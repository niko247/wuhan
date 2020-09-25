package com.github.niko247;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OldCasesManager {
    private Path tempFilePath;
    private static final Type LIST_TYPE = new TypeToken<ArrayList<CoronaCase>>() {
    }.getType();

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

    public Optional<List<CoronaCase>> get() throws IOException {
        if (Files.exists(tempFilePath)) {
            var jsonContent = Files.readString(tempFilePath);
            return Optional.of(new Gson().fromJson(jsonContent, LIST_TYPE));
        } else {
            return Optional.empty();
        }
    }

    public void save(List<CoronaCase> cases) throws IOException {
        var jsonContent = new Gson().toJson(cases);
        Files.writeString(tempFilePath, jsonContent);
    }

}
