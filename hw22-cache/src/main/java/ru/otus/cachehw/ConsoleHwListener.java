package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleHwListener<K, V> implements HwListener<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleHwListener.class);

    @Override
    public void notify(K key, V value, String action) {
        logger.info("Cache event: key={}, value={}, action={}", key, value, action);
    }
}
