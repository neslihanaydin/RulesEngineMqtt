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
    /*
    @Test
    void testCalculateBaseAmount_ReturnsZero_WhenNotEligible() {
        // Not eligible
        SummerSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, 10000, false);
        assertEquals(0F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns250_WhenMarriedWithoutChildrenAndHouseholdIncomeLessThan80000() {
        // Married without children
        SummerSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.MARRIED, 79999, true);
        assertEquals(250F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns250_WhenMarriedWithoutChildrenAndHouseholdIncomeEqualTo80000() {
        // Married without children
        SummerSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.MARRIED, 80000, true);
        assertEquals(250F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_ReturnsZero_WhenMarriedWithoutChildrenAndHouseholdIncomeGreaterThan80000() {
        // Married without children
        SummerSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.MARRIED, 80001, true);
        assertEquals(0F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns100_WhenMarriedWithChildrenAndHouseholdIncomeLessThan90000() {
        // Married with children
        SummerSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.MARRIED, 89999, true);
        assertEquals(100F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns100_WhenMarriedWithChildrenAndHouseholdIncomeEqualTo90000() {
        // Married with children
        SummerSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.MARRIED, 90000, true);
        assertEquals(100F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_ReturnsZero_WhenMarriedWithChildrenAndHouseholdIncomeGreaterThan90000() {
        // Married with children
        SummerSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.MARRIED, 90001, true);
        assertEquals(0F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns100_WhenSingleParentWithChildrenAndHouseholdIncomeLessThan90000() {
        // Single with children
        SummerSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, 89999, true);
        assertEquals(100F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns100_WhenSingleParentWithChildrenAndHouseholdIncomeEqualTo90000() {
        // Single with children
        SummerSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, 90000, true);
        assertEquals(100F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_ReturnsZero_WhenSingleParentWithChildrenAndHouseholdIncomeGreaterThan90000() {
        // Single with children
        SummerSupplementEligibilityInput input = createInput("1", 1, FamilyComposition.SINGLE, 90001, true);
        assertEquals(0F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns150_WhenSingleWithoutChildrenAndHouseholdIncomeLessThan50000() {
        // Single without children
        SummerSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.SINGLE, 49999, true);
        assertEquals(150F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_Returns150_WhenSingleWithoutChildrenAndHouseholdIncomeEqualTo50000() {
        // Single without children
        SummerSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.SINGLE, 50000, true);
        assertEquals(150F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }

    @Test
    void testCalculateBaseAmount_ReturnsZero_WhenSingleWithoutChildrenAndHouseholdIncomeGreaterThan50000() {
        // Single without children
        SummerSupplementEligibilityInput input = createInput("1", 0, FamilyComposition.SINGLE, 50001, true);
        assertEquals(0F, BaseAmountCalculator.calculate(input, Supplement.SUMMER));
    }
     */
}