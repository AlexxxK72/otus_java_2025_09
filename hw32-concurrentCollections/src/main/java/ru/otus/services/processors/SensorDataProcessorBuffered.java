package ru.otus.services.processors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

// Этот класс нужно реализовать
@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> dataBuffer;
    private final Object flushLock = new Object();

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new PriorityBlockingQueue<>(bufferSize, Comparator.comparing(SensorData::getMeasurementTime));
    }

    @Override
    public void process(SensorData data) {
        dataBuffer.add(data);
        if (dataBuffer.size() >= bufferSize) {
            flush();
        }
    }

    public void flush() {
        synchronized (flushLock) {
            try {
                List<SensorData> bufferedData = new ArrayList<>(bufferSize);
                dataBuffer.drainTo(bufferedData, bufferSize);
                if (!bufferedData.isEmpty()) {
                    writer.writeBufferedData(bufferedData);
                }
            } catch (Exception e) {
                log.error("Ошибка в процессе записи буфера", e);
            }
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
