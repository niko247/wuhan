package com.github.niko247;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class OldCasesManager {
    private Path tempFilePath;
    private ObjectMapper mapper;

    public OldCasesManager() {
        this("corona_temp.json");
    }

    public OldCasesManager(String tempFilePath) {
        setPath(tempFilePath);
        mapper = new ObjectMapper();
    }

    public void setPath(String path) {
        this.tempFilePath = Path.of(path);
    }

    public void setPath(Path path) {
        this.tempFilePath = path;
    }

    public Optional<SummaryResults> get() throws IOException {
        if (Files.exists(tempFilePath)) {
            var jsonContent = Files.readString(tempFilePath);
            return Optional.of(mapper.readValue(jsonContent, SummaryResults.class));
        } else {
            return Optional.empty();
        }
    }

    public void save(SummaryResults cases) throws IOException {
        var jsonContent = mapper.writeValueAsString(cases);
        Files.writeString(tempFilePath, jsonContent);
    }

}
