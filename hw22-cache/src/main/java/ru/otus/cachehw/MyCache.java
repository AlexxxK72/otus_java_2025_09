package ru.otus.cachehw;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private final Map<K, WeakReference<V>> cache = new WeakHashMap<>();
    private final ReferenceQueue<V> valueQueue = new ReferenceQueue<>();
    private final List<HwListener<K, V>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cleanup();
        cache.put(key, new WeakValueRef<>(key, value, valueQueue));
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        cleanup();
        WeakReference<V> ref = cache.get(key);
        if (ref != null) {
            V value = ref.get();
            cache.remove(key);
            notifyListeners(key, value, "remove");
        }
    }

    @Override
    public V get(K key) {
        cleanup();
        WeakReference<V> ref = cache.get(key);
        return ref != null ? ref.get() : null;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(K key, V value, String put) {
        for (HwListener<K, V> listener : listeners) {
            listener.notify(key, value, put);
        }
    }

    private void cleanup() {
        Reference<? extends V> ref;
        while ((ref = valueQueue.poll()) != null) {
            @SuppressWarnings("unchecked")
            WeakValueRef<K, V> weakRef = (WeakValueRef<K, V>) ref;
            K key = weakRef.key;
            V value = weakRef.get();
            notifyListeners(key, value, "remove gc");
        }
    }

    private static class WeakValueRef<K, V> extends WeakReference<V> {
        private final K key;

        WeakValueRef(K key, V value, ReferenceQueue<? super V> queue) {
            super(value, queue);
            this.key = key;
        }
    }
}
