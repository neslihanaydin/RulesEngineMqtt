package org.neslihantrpc.util;

import com.google.gson.*;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;

import java.lang.reflect.Type;

public class SummerSupplementEligibilityInputDeserializer implements JsonDeserializer<SummerSupplementEligibilityInput> {

    @Override
    public SummerSupplementEligibilityInput deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (!jsonObject.has("id") ||
                !jsonObject.has("householdIncome") ||
                !jsonObject.has("familyComposition") ||
                !jsonObject.has("numberOfChildren") ||
                !jsonObject.has("familyUnitInPayForJuly")
        ) {
            throw new JsonParseException("Missing required fields");
        }

        String id = jsonObject.get("id").getAsString();
        int numberOfChildren = jsonObject.get("numberOfChildren").getAsInt();

        JsonElement familyCompositionElement = jsonObject.get("familyComposition");
        FamilyComposition familyComposition = jsonDeserializationContext.deserialize(familyCompositionElement, FamilyComposition.class);

        Float householdIncome = jsonObject.get("householdIncome").getAsFloat();
        boolean familyUnitInPayForJuly = jsonObject.get("familyUnitInPayForJuly").getAsBoolean();

        return new SummerSupplementEligibilityInput(id, numberOfChildren, familyComposition, householdIncome, familyUnitInPayForJuly);
    }
}