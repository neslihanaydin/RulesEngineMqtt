package org.neslihantrpc.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChildrenAmountCalculatorSummerTest {

    private static SummerSupplementEligibilityInput createInput(int numberOfChildren, FamilyComposition familyComposition, boolean familyUnitInPayForJuly) {
        return new SummerSupplementEligibilityInput("1", numberOfChildren, familyComposition, (float) 10000, familyUnitInPayForJuly);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of("Not eligible", createInput(1, FamilyComposition.SINGLE, false), 0F),
                Arguments.of("Single without children", createInput(0, FamilyComposition.SINGLE, true), 0F),
                Arguments.of("Married without children", createInput(0, FamilyComposition.MARRIED, true), 0F),
                Arguments.of("Married with children", createInput(1, FamilyComposition.MARRIED, true), 50F),
                Arguments.of("Single with children", createInput(1, FamilyComposition.SINGLE, true), 50F)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideTestCases")
    @DisplayName("Test Children Amount Calculation Summer")
    void testCalculateChildrenAmount(String testName, SummerSupplementEligibilityInput input, float expectedAmount) {
        assertEquals(expectedAmount, ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForJuly(), Supplement.SUMMER));
    }
}
