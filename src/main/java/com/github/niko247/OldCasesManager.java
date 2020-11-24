package com.github.niko247;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class OldCasesManager {
    private Path tempFilePath;

    public OldCasesManager() {
        this("corona_temp.json");
    }

    public OldCasesManager(String tempFilePath) {
        setPath(tempFilePath);
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
            return Optional.of(new Gson().fromJson(jsonContent, SummaryResults.class));
        } else {
            return Optional.empty();
        }
    }

    public void save(SummaryResults cases) throws IOException {
        var jsonContent = new Gson().toJson(cases);
        Files.writeString(tempFilePath, jsonContent);
    }

}
