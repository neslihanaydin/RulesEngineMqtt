package org.neslihantrpc.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChildrenAmountCalculatorWinterTest {

    private static WinterSupplementEligibilityInput createInput(int numberOfChildren, FamilyComposition familyComposition, boolean familyUnitInPayForDecember) {
        return new WinterSupplementEligibilityInput("1", numberOfChildren, familyComposition, familyUnitInPayForDecember);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("Not eligible", createInput(1, FamilyComposition.SINGLE, false), 0F),
                Arguments.of("Single without children", createInput(0, FamilyComposition.SINGLE, true), 0F),
                Arguments.of("Married without children", createInput(0, FamilyComposition.MARRIED, true), 0F),
                Arguments.of("Married with children", createInput(1, FamilyComposition.MARRIED, true), 20F),
                Arguments.of("Single with children", createInput(1, FamilyComposition.SINGLE, true), 20F)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCases")
    @DisplayName("Test Children Amount Calculation Winter")
    void testCalculateChildrenAmount(String testName, WinterSupplementEligibilityInput input, float expectedAmount) {
        assertEquals(expectedAmount, ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForDecember(), Supplement.WINTER));
    }

    /*
    @Test
    void testCalculateChildrenAmount_ReturnsZero_WhenNotEligible() {
        // Not eligible
        WinterSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, false);
        assertEquals(0F, ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForDecember(), Supplement.WINTER));
    }

    @Test
    void testCalculateChildrenAmount_ReturnsZero_WhenNumberOfChildrenIsNull() {
        // Not eligible
        WinterSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, false);
        assertEquals(0F, ChildrenAmountCalculator.calculate(null, input.getFamilyUnitInPayForDecember(), Supplement.WINTER));
    }

    @Test
    void testCalculateChildrenAmount_ReturnsZero_WhenFamilyUnitInPayIsNull() {
        // Not eligible
        WinterSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, false);
        assertEquals(0F, ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), null, Supplement.WINTER));
    }

    @Test
    void testCalculateChildrenAmount_Returns0_WithoutChildren() {
        WinterSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.SINGLE, true);
        assertEquals(0F, ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForDecember(), Supplement.WINTER));
    }
     */
}
