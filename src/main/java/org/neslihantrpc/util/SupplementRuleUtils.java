package org.neslihantrpc.util;

import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;

public class SupplementRuleUtils {

    public static SupplementEligibilityOutput createEligibilityOutput(WinterSupplementEligibilityInput input, Supplement supplementType) {
        if (input == null || supplementType == null) {
            throw new IllegalArgumentException("Input or supplementType cannot be null");
        }

        float childrenAmount = ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForDecember(), supplementType);
        float baseAmount = BaseAmountCalculator.calculate(input, supplementType);

        return new SupplementEligibilityOutput(
                input.getId(),
                input.getFamilyUnitInPayForDecember(),
                childrenAmount,
                baseAmount,
                supplementType);
    }

    public static SupplementEligibilityOutput createEligibilityOutput(SummerSupplementEligibilityInput input, Supplement supplementType) {
        if (input == null || supplementType == null) {
            throw new IllegalArgumentException("Input or supplementType cannot be null");
        }

        float childrenAmount = ChildrenAmountCalculator.calculate(input.getNumberOfChildren(), input.getFamilyUnitInPayForJuly(), supplementType);
        float baseAmount = BaseAmountCalculator.calculate(input, supplementType);

        return new SupplementEligibilityOutput(
                input.getId(),
                input.getFamilyUnitInPayForJuly(),
                childrenAmount,
                baseAmount,
                supplementType);
    }
}