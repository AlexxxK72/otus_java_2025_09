package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Counter {
    private static final Logger logger = LoggerFactory.getLogger(Counter.class);
    private int last = 2;

    private synchronized void action(int numberThread) {
        int current = 1;
        boolean orderAsc = true;
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (last == numberThread) {
                    this.wait();
                }

                logger.info(current + " :Thread-" + numberThread);

                if (orderAsc) {
                    current++;
                    if (current == 10) {
                        orderAsc = false;
                    }
                } else {
                    current--;
                    if (current == 1) {
                        orderAsc = true;
                    }
                }
                last = numberThread;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Counter counter = new Counter();
        new Thread(() -> counter.action(1)).start();
        new Thread(() -> counter.action(2)).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
