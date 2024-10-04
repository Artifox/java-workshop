package com.example.teamcity.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Config {

    public static final String CONFIG_PROPERTIES_FILE = "config.properties";
    private static Config config;
    private Properties properties;

    private Config() {
        properties = new Properties();
        loadProperties(CONFIG_PROPERTIES_FILE);
    }

    private static Config getConfig() {
        if (config == null) {
            config = new Config();
        }

        return config;
    }

    public void loadProperties(String fileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                throw new RuntimeException("Could not find file: " + fileName);
            }
            Objects.requireNonNull(properties).load(inputStream);
        } catch (IOException e) {
            System.err.println("Error during file reading: " + fileName);
            throw new RuntimeException(e);
        }
    }

    public static String getProperty(String key) {
        return getConfig().properties.getProperty(key);
    }
}
