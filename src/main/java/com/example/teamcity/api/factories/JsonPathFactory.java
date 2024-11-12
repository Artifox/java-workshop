package com.example.teamcity.api.factories;

import com.example.teamcity.api.models.BuildType;
import com.example.teamcity.api.models.Project;

import java.util.HashMap;
import java.util.Map;

public class JsonPathFactory {

    private static final Map<Class<?>, String> jsonPaths = new HashMap<>();

    static {
        jsonPaths.put(BuildType.class, "buildType");
        jsonPaths.put(Project.class, "project");
    }

    public static String getJsonPath(Class<?> clazz) {
        String jsonPath = jsonPaths.get(clazz);
        if (jsonPath == null) {
            throw new IllegalArgumentException("No JSON path found for class: " + clazz.getSimpleName());
        }
        return jsonPath;
    }
}
