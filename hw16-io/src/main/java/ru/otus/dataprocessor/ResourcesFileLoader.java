package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import ru.otus.model.Measurement;

public class ResourcesFileLoader implements Loader {

    private final ObjectMapper mapper;
    private final String fileName;

    public ResourcesFileLoader(String fileName) {
        mapper = new ObjectMapper();
        this.fileName = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
        try (var is = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (is == null) {
                throw new IllegalArgumentException("Resource not found: " + fileName);
            }
            return mapper.readValue(is, mapper.getTypeFactory().constructCollectionType(List.class, Measurement.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
