package org.neslihantrpc.util;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

public class JsonHandler {

    private static final Gson gson = new Gson();

    private JsonHandler() {
        throw new IllegalStateException("Utility class");
    }

    public static String toJson(Object object) {
        try {
            return gson.toJson(object);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error serializing object to JSON: " + e.getMessage(), e);
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return gson.fromJson(json, clazz);
        } catch (JsonParseException e) {
            throw new IllegalArgumentException("Error deserializing JSON to object: " + e.getMessage(), e);
        }
    }
}
