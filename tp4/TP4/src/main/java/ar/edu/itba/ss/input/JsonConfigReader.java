package ar.edu.itba.ss.input;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonConfigReader {

    private final ObjectMapper objectMapper;

    public JsonConfigReader() {
        this.objectMapper = new ObjectMapper();
    }

    public <T> T readConfig(String filePath, Class<T> configClass) throws IOException {
        return objectMapper.readValue(new File(filePath), configClass);
    }
}