package org.neslihantrpc;

import org.junit.jupiter.api.Test;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.util.BaseAmountCalculator;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseAmountCalculatorTest {

    private WinterSupplementEligibilityInput createInput(String id, int numberOfChildren, FamilyComposition familyComposition, boolean familyUnitInPayForDecember) {
        return new WinterSupplementEligibilityInput(id, numberOfChildren, familyComposition, familyUnitInPayForDecember);
    }

    @Test
    void testCalculateBaseAmount_ReturnsZero_WhenNotEligible() {
        // Not eligible
        WinterSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, false);
        assertEquals(0F, BaseAmountCalculator.calculate(input, Supplement.WINTER));
    }

    @Test
    void testCalculateBaseAmount_Returns120_WhenMarriedWithoutChildren() {
        // Married without children
        WinterSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.MARRIED, true);
        assertEquals(120F, BaseAmountCalculator.calculate(input, Supplement.WINTER));
    }

    @Test
    void testCalculateBaseAmount_Returns120_WhenMarriedWithChildren() {
        // Married with children
        WinterSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.MARRIED, true);
        assertEquals(120F, BaseAmountCalculator.calculate(input, Supplement.WINTER));
    }

    @Test
    void testCalculateBaseAmount_Returns120_WhenSingleParentWithChildren() {
        // Single with children
        WinterSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, true);
        assertEquals(120F, BaseAmountCalculator.calculate(input, Supplement.WINTER));
    }

    @Test
    void testCalculateBaseAmount_Returns60_WhenSingleWithoutChildren() {
        // Single without children
        WinterSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.SINGLE, true);
        assertEquals(60F, BaseAmountCalculator.calculate(input, Supplement.WINTER));
    }
}