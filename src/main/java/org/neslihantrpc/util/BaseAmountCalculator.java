package org.neslihantrpc.util;

import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;

public class BaseAmountCalculator {

    public static float calculate(SupplementEligibilityInput input, Supplement supplementType) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }
        try {
            return switch (supplementType) {
                case WINTER -> calculateWinterSupplementBaseAmount((WinterSupplementEligibilityInput) input);
                case SUMMER -> calculateSummerSupplementBaseAmount((SummerSupplementEligibilityInput) input);
                default -> 0F;
            };
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Input type is not valid for the supplement type");
        }
    }

    private static float calculateWinterSupplementBaseAmount(WinterSupplementEligibilityInput input) {
        if (!input.getFamilyUnitInPayForDecember()) {
            return 0F;
        }
        if (input.getFamilyComposition() == FamilyComposition.MARRIED || input.getNumberOfChildren() > 0) {
            return 120F;
        }

        if (input.getFamilyComposition() == FamilyComposition.SINGLE) {
            return 60F;
        }
        return 0F;
    }

    private static float calculateSummerSupplementBaseAmount(SummerSupplementEligibilityInput input) {
        if (!input.getFamilyUnitInPayForJuly()) {
            return 0F;
        }
        if (input.getFamilyComposition() == FamilyComposition.MARRIED && input.getNumberOfChildren() == 0 && input.getHouseholdIncome() <= 80000) {
            return 250F;
        }

        if (input.getNumberOfChildren() > 0 && input.getHouseholdIncome() <= 90000) {
            return 100F;
        }

        if (input.getFamilyComposition() == FamilyComposition.SINGLE && input.getNumberOfChildren() == 0 && input.getHouseholdIncome() <= 50000) {
            return 150F;
        }

        return 0F;
    }
}