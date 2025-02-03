package org.neslihantrpc.util;

import com.google.gson.*;
import org.neslihantrpc.enums.FamilyComposition;

import java.lang.reflect.Type;

public class FamilyCompositionDeserializer implements JsonDeserializer<FamilyComposition> {
    @Override
    public FamilyComposition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        String value = json.getAsString().toUpperCase(); // Convert input to uppercase
        try {
            return FamilyComposition.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Invalid FamilyComposition value: " + value);
        }
    }
}
