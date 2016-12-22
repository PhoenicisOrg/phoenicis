package com.playonlinux.tools.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

public class CompatibleConfigFileFormatFactory {
    private final ObjectMapper objectMapper;

    public CompatibleConfigFileFormatFactory(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public ConfigFile open(String filePath) {
        return new CompatibleConfigFileFormat(objectMapper, new File(filePath));
    }

    public ConfigFile open(File file) {
        return new CompatibleConfigFileFormat(objectMapper, file);
    }
}
