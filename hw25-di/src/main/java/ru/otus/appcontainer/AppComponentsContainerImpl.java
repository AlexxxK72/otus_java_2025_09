package ru.otus.appcontainer;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        try {
            processConfig(initialConfigClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        if (initialConfigClasses == null || initialConfigClasses.length == 0) {
            throw new IllegalArgumentException("initialConfigClasses cannot be null or empty");
        }
        for (Class<?> initialConfigClass : initialConfigClasses) {
            checkConfigClass(initialConfigClass);
        }
        List<Class<?>> sortedConfigs = Arrays.stream(initialConfigClasses)
                .sorted(Comparator.comparingInt(
                        o -> o.getAnnotation(AppComponentsContainerConfig.class).order()))
                .toList();

        try {
            for (Class<?> initialConfigClass : sortedConfigs) {
                processConfig(initialConfigClass);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void processConfig(Class<?> configClass)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {
        checkConfigClass(configClass);
        List<Method> appComponentAnnotatedMethods = getAppComponentAnnotatedMethods(configClass);
        Constructor<?> constructor = getConstructorWithoutParameters(configClass);
        Object config = constructor.newInstance();
        appComponentAnnotatedMethods.forEach(appComponent -> addAppComponent(appComponent, config));
    }

    private Constructor<?> getConstructorWithoutParameters(Class<?> configClass) {
        return Arrays.stream(configClass.getConstructors())
                .filter(constructor -> constructor.getParameterCount() == 0)
                .findFirst()
                .map(constructor -> {
                    constructor.setAccessible(true);
                    return constructor;
                })
                .orElseThrow(() -> new RuntimeException("No constructor without parameters, found for " + configClass));
    }

    private List<Method> getAppComponentAnnotatedMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(
                        o -> o.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private Object[] getParameters(Method appComponent) {
        Parameter[] parameters = appComponent.getParameters();
        if (parameters.length == 0) {
            return new Object[0];
        }
        Object[] params = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            params[i] = getAppComponent(parameters[i].getType());
        }
        return params;
    }

    private void addAppComponent(Method appComponent, Object config) {
        try {
            String name = appComponent.getAnnotation(AppComponent.class).name();
            if (appComponentsByName.containsKey(name)) {
                throw new RuntimeException(String.format(
                        "Duplicate @AppComponent name '%s' in config %s",
                        name, config.getClass().getName()));
            }
            Object[] parameters = getParameters(appComponent);
            Object instance = appComponent.invoke(config, parameters);
            appComponentsByName.put(name, instance);
            appComponents.add(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        List<Object> matching =
                appComponents.stream().filter(componentClass::isInstance).toList();

        if (matching.isEmpty()) {
            throw new RuntimeException("Can't find component of type " + componentClass.getName());
        }

        if (matching.size() > 1) {
            throw new RuntimeException("Found more than one component of type " + componentClass.getName()
                    + ": count = " + matching.size());
        }

        return componentClass.cast(matching.getFirst());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName);
        if (component == null) {
            throw new RuntimeException("Component '" + componentName + "' not found");
        }
        return (C) component;
    }
}
