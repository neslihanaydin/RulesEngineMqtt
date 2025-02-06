package org.neslihantrpc.util;

import com.google.gson.*;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;

import java.lang.reflect.Type;

public class WinterSupplementEligibilityInputDeserializer implements JsonDeserializer<WinterSupplementEligibilityInput> {

    @Override
    public WinterSupplementEligibilityInput deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (!jsonObject.has("id") ||
                !jsonObject.has("numberOfChildren") ||
                !jsonObject.has("familyComposition") ||
                !jsonObject.has("familyUnitInPayForDecember")
        ) {
            throw new JsonParseException("Missing required fields");
        }

        String id = jsonObject.get("id").getAsString();
        int numberOfChildren = jsonObject.get("numberOfChildren").getAsInt();
        FamilyComposition familyComposition = FamilyComposition.valueOf(jsonObject.get("familyComposition").getAsString());
        boolean familyUnitInPayForDecember = jsonObject.get("familyUnitInPayForDecember").getAsBoolean();

        return new WinterSupplementEligibilityInput(id, numberOfChildren, familyComposition, familyUnitInPayForDecember);
    }
}