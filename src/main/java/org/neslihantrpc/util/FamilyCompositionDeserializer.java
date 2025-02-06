package org.neslihantrpc.util;

import com.google.gson.*;
import org.neslihantrpc.enums.FamilyComposition;

import java.lang.reflect.Type;

public class FamilyCompositionDeserializer implements JsonDeserializer<FamilyComposition> {
    @Override
    public FamilyComposition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return FamilyComposition.fromString(json.getAsString());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Invalid FamilyComposition value: " + json.getAsString());
        }
    }
}
