package org.neslihantrpc.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BaseAmountCalculatorSummerTest {

    private static SummerSupplementEligibilityInput createInput(int numberOfChildren, FamilyComposition familyComposition, float householdIncome, boolean familyUnitInPayForJuly) {
        return new SummerSupplementEligibilityInput("1", numberOfChildren, familyComposition, householdIncome, familyUnitInPayForJuly);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("Not eligible", createInput(1, FamilyComposition.SINGLE, 10000, false), 0F),
                Arguments.of("Married without children, income < 80000", createInput(0, FamilyComposition.MARRIED, 79999, true), 250F),
                Arguments.of("Married without children, income = 80000", createInput(0, FamilyComposition.MARRIED, 80000, true), 250F),
                Arguments.of("Married without children, income > 80000", createInput(0, FamilyComposition.MARRIED, 80001, true), 0F),
                Arguments.of("Married with children, income < 90000", createInput(1, FamilyComposition.MARRIED, 89999, true), 100F),
                Arguments.of("Married with children, income = 90000", createInput(1, FamilyComposition.MARRIED, 90000, true), 100F),
                Arguments.of("Married with children, income > 90000", createInput(1, FamilyComposition.MARRIED, 90001, true), 0F),
                Arguments.of("Single parent with children, income < 90000", createInput(1, FamilyComposition.SINGLE, 89999, true), 100F),
                Arguments.of("Single parent with children, income = 90000", createInput(1, FamilyComposition.SINGLE, 90000, true), 100F),
                Arguments.of("Single parent with children, income > 90000", createInput(1, FamilyComposition.SINGLE, 90001, true), 0F),
                Arguments.of("Single without children, income < 50000", createInput(0, FamilyComposition.SINGLE, 49999, true), 150F),
                Arguments.of("Single without children, income = 50000", createInput(0, FamilyComposition.SINGLE, 50000, true), 150F),
                Arguments.of("Single without children, income > 50000", createInput(0, FamilyComposition.SINGLE, 50001, true), 0F)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCases")
    @DisplayName("Test Base Amount Calculation Summer")
    void testCalculateBaseAmount(String testName, SummerSupplementEligibilityInput input, float expectedAmount) {
        assertEquals(expectedAmount, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    @DisplayName("Test Calculate Base Amount Throws Exception When Input is Null")
    void testCalculateBaseAmount_ThrowsException_WhenInputIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                BaseAmountCalculator.calculate(null, Supplement.SUMMER)
        );

        assertEquals("Input cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Test Calculate Base Amount Throws Exception When Supplement Type and Input Type Does Not Matched")
    void testCalculateBaseAmount_ThrowsException_WhenSupplementTypeAndInputTypeDoesNotMatched() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                BaseAmountCalculator.calculate(createInput(0, FamilyComposition.MARRIED, 10000, true), Supplement.WINTER)
        );

        assertEquals("Input type is not valid for the supplement type", exception.getMessage());
    }
}