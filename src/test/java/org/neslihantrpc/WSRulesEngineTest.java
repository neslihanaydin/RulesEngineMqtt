package org.neslihantrpc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.neslihantrpc.engine.RulesEngine;
import org.neslihantrpc.enums.ConfigType;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.neslihantrpc.mqtt.MqttConfig;

import static org.junit.jupiter.api.Assertions.*;

public class WSRulesEngineTest {
    RulesEngine engine;

    @BeforeEach
    public void setup() {
        MqttConfig.current.setEnvironment(ConfigType.TEST);
        RulesEngineFactory factory = new RulesEngineFactory();
        engine = factory.create(Supplement.WINTER);
    }

    @Test
    void testEligibilityForSinglePersonWithoutDependentChildren() {
        WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("1", 0, FamilyComposition.SINGLE, true);
        SupplementEligibilityOutput output = engine.process(input);

        assertNotNull(output);
        assertTrue(output.getIsEligible());
        assertEquals(60F, output.getSupplementAmount());
        assertEquals(input.getId(), output.getId());
    }

    @Test
    void testEligibilityForChildlessCouple() {
        WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("2", 0, FamilyComposition.MARRIED, true);
        SupplementEligibilityOutput output = engine.process(input);

        assertNotNull(output);
        assertTrue(output.getIsEligible());
        assertEquals(120F, output.getSupplementAmount());
        assertEquals(input.getId(), output.getId());
    }

    @Test
    void testEligibilityForSingleParentWithDependentChildren() {
        WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("3", 1, FamilyComposition.SINGLE, true);
        SupplementEligibilityOutput output = engine.process(input);

        assertNotNull(output);
        assertTrue(output.getIsEligible());
        assertEquals(120F + 20F * input.getNumberOfChildren(), output.getSupplementAmount());
        assertEquals(input.getId(), output.getId());
    }

    @Test
    void testEligibilityForTwoParentWithDependentChildren() {
        WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("4", 1, FamilyComposition.MARRIED, true);
        SupplementEligibilityOutput output = engine.process(input);

        assertNotNull(output);
        assertTrue(output.getIsEligible());
        assertEquals(120F + 20F * input.getNumberOfChildren(), output.getSupplementAmount());
        assertEquals(input.getId(), output.getId());
    }
    @Test
    void testEligibilityForNotEligible() {
        WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("5", 1, FamilyComposition.SINGLE, false);
        SupplementEligibilityOutput output = engine.process(input);

        assertNotNull(output);
        assertFalse(output.getIsEligible());
        assertEquals(0F, output.getSupplementAmount());
        assertEquals(input.getId(), output.getId());
    }

    @Test
    void testEligibilityForNegativeChildren() {
        assertThrows(IllegalArgumentException.class, () -> engine.process(
                new WinterSupplementEligibilityInput("6", -1, FamilyComposition.SINGLE, true)
        ));
    }

    @Test
    void testEligibilityWithNullInput() {
        assertThrows(NullPointerException.class, () -> engine.process(null));
    }
}