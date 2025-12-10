package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileSerializer implements Serializer {

    private final ObjectMapper mapper;
    private final String fileName;

    public FileSerializer(String fileName) {
        mapper = new ObjectMapper();
        // сделано в учебных целях, мапу сортируем два раза
        mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        // формирует результирующий json и сохраняет его в файл
        try {
            mapper.writeValue(new File(fileName), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
