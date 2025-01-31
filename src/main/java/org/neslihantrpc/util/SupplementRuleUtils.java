package org.neslihantrpc.util;

import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;

public class SupplementRuleUtils {

    public static SupplementEligibilityOutput createEligibilityOutput(WinterSupplementEligibilityInput input, Supplement supplementType) {
        System.out.println("Processing winter rules in SupplementRuleUtils");
        if (input == null || supplementType == null) {
            throw new IllegalArgumentException("Input or supplementType cannot be null");
        }

        float childrenAmount;
        float baseAmount;
        if (input.getFamilyUnitInPayForDecember()) {
            childrenAmount = ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForDecember(), supplementType);
            baseAmount = BaseAmountCalculator.calculate(input, supplementType);
        } else {
            childrenAmount = 0;
            baseAmount = 0;
        }

        return new SupplementEligibilityOutput(
                input.getId(),
                input.getFamilyUnitInPayForDecember(),
                childrenAmount,
                baseAmount,
                supplementType);
    }

    public static SupplementEligibilityOutput createEligibilityOutput(SummerSupplementEligibilityInput input, Supplement supplementType) {
        System.out.println("Processing summer rules in SupplementRuleUtils");
        if (input == null || supplementType == null) {
            throw new IllegalArgumentException("Input or supplementType cannot be null");
        }

        float childrenAmount;
        float baseAmount;
        if (input.getFamilyUnitInPayForJuly()) {
            childrenAmount = ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForJuly(), supplementType);
            baseAmount = BaseAmountCalculator.calculate(input, supplementType);
        } else {
            childrenAmount = 0;
            baseAmount = 0;
        }

        return new SupplementEligibilityOutput(
                input.getId(),
                input.getFamilyUnitInPayForJuly(),
                childrenAmount,
                baseAmount,
                supplementType);
    }
}