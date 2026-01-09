package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import ru.otus.jdbc.annotation.Id;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private final Class<T> entityClass;
    private final Field idField;
    private final List<Field> allFields;
    private final Constructor<T> constructor;

    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.constructor = constructor();
        this.allFields = allFields();
        this.idField = idField();
    }

    @Override
    public String getName() {
        return entityClass.getSimpleName().toLowerCase();
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return allFields.stream().filter(field -> !field.equals(idField)).toList();
    }

    private Constructor<T> constructor() {
        try {
            Constructor<T> ctor = entityClass.getDeclaredConstructor();
            ctor.setAccessible(true);
            return ctor;
        } catch (Exception e) {
            throw new RuntimeException("Could not get no-args constructor", e);
        }
    }

    private List<Field> allFields() {
        return Arrays.stream(entityClass.getDeclaredFields())
                .peek(field -> field.setAccessible(true))
                .toList();
    }

    private Field idField() {
        var fields = allFields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .toList();
        if (fields.size() != 1) {
            throw new RuntimeException("Entity must have exactly one @Id field");
        }
        return fields.getFirst();
    }
}
