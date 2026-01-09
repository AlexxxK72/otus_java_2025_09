package ru.otus.cachehw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HWCacheDemo {
    private static final Logger logger = LoggerFactory.getLogger(HWCacheDemo.class);

    public static void main(String[] args) throws InterruptedException {
        new HWCacheDemo().demo();
    }

    private void demo() throws InterruptedException {
        HwCache<String, Integer> cache = new MyCache<>();

        ConsoleHwListener<String, Integer> listener = new ConsoleHwListener<>();
        cache.addListener(listener);
        cache.put("1", 1);
        cache.put("2", 133);

        logger.info("getValue:{}", cache.get("1"));
        cache.remove("1");

        System.gc();
        Thread.sleep(1000);
        logger.info("getValue:{}", cache.get("2"));

        cache.removeListener(listener);
    }
}
