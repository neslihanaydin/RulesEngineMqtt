package org.neslihantrpc.util;

import org.junit.jupiter.api.Test;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;

import static org.junit.jupiter.api.Assertions.*;

public class JsonHandlerTest {

    @Test
    public void testToJson() {
        // Create a new SummerSupplementEligibilityInput object
        SummerSupplementEligibilityInput input = new SummerSupplementEligibilityInput("1", 2, FamilyComposition.SINGLE, 10000F, true);

        String json = JsonHandler.toJson(input);
        assertNotNull(json);

        assertTrue(json.contains("\"id\":\"1\""));
        assertTrue(json.contains("\"numberOfChildren\":2"));
        assertTrue(json.contains("\"familyComposition\":\"SINGLE\""));
        assertTrue(json.contains("\"householdIncome\":10000.0"));
        assertTrue(json.contains("\"familyUnitInPayForJuly\":true"));
    }

    @Test
    public void testFromJson() {
        // Create a JSON string
        String json = "{\"id\":\"1\",\"numberOfChildren\":2,\"familyComposition\":\"single\",\"householdIncome\":5000.0,\"familyUnitInPayForJuly\":true}";

        SummerSupplementEligibilityInput input = JsonHandler.fromJson(json, SummerSupplementEligibilityInput.class);

        assertNotNull(input);

        assertEquals("1", input.getId());
        assertEquals(2, input.getNumberOfChildren());
        assertEquals(FamilyComposition.SINGLE, input.getFamilyComposition());
        assertEquals(5000.0F, input.getHouseholdIncome());
        assertTrue(input.getFamilyUnitInPayForJuly());
    }

    @Test
    public void testFromJsonWithMissingRequiredFields() {
        // Create an invalid JSON string (missing a required field)
        String invalidJson = "{\"id\":\"1\",\"numberOfChildren\":2}";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                JsonHandler.fromJson(invalidJson, SummerSupplementEligibilityInput.class)
        );

        assertEquals("Error deserializing JSON to object: Missing required fields", exception.getMessage());
    }

    @Test
    public void testFromJsonEmpty() {
        String emptyJson =  "{}";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                JsonHandler.fromJson(emptyJson, SummerSupplementEligibilityInput.class)
        );

        assertEquals("Error deserializing JSON to object: Missing required fields", exception.getMessage());

    }
}