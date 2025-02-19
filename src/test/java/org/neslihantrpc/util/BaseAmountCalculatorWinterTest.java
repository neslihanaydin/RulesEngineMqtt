package org.neslihantrpc.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseAmountCalculatorWinterTest {

    private static WinterSupplementEligibilityInput createInput(int numberOfChildren, FamilyComposition familyComposition, boolean familyUnitInPayForDecember) {
        return new WinterSupplementEligibilityInput("1", numberOfChildren, familyComposition, familyUnitInPayForDecember);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("Not eligible", createInput(1, FamilyComposition.SINGLE, false), 0F),
                Arguments.of("Married without children", createInput(0, FamilyComposition.MARRIED, true), 120F),
                Arguments.of("Married with children", createInput(1, FamilyComposition.MARRIED, true), 120F),
                Arguments.of("Single with children", createInput(1, FamilyComposition.SINGLE, true), 120F),
                Arguments.of("Single without children", createInput(0, FamilyComposition.SINGLE, true), 60F)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCases")
    @DisplayName("Test Base Amount Calculation Winter")
    void testCalculateBaseAmount(String testName, WinterSupplementEligibilityInput input, float expectedAmount) {
        assertEquals(expectedAmount, BaseAmountCalculator.calculate(input, Supplement.WINTER));
    }

    @Test
    @DisplayName("Test Calculate Base Amount Throws Exception When Input is Null")
    void testCalculateBaseAmount_ThrowsException_WhenInputIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                BaseAmountCalculator.calculate(null, Supplement.WINTER)
        );

        assertEquals("Input cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Test Calculate Base Amount Throws Exception When Supplement Type and Input Type Does Not Matched")
    void testCalculateBaseAmount_ThrowsException_WhenSupplementTypeAndInputTypeDoesNotMatched() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                BaseAmountCalculator.calculate(createInput(0, FamilyComposition.MARRIED, true), Supplement.SUMMER)
        );

        assertEquals("Input type is not valid for the supplement type", exception.getMessage());
    }
}