package org.neslihantrpc.util;

import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.model.WinterSupplementEligibilityOutput;

public class WinterSupplementRuleUtils {

    public static WinterSupplementEligibilityOutput createEligibilityOutput(WinterSupplementEligibilityInput input) {
        WinterSupplementEligibilityOutput output = new WinterSupplementEligibilityOutput();
        output.setId(input.getId());
        output.setEligible(input.getFamilyUnitInPayForDecember());
        output.setChildrenAmount(input.getNumberOfChildren());
        output.setBaseAmount(calculateBaseAmount(input.getFamilyComposition(), input.getNumberOfChildren(), input.getFamilyUnitInPayForDecember()));

        return output;
    }

    private static float calculateBaseAmount(FamilyComposition familyComposition, Integer numberOfChildren, Boolean familyUnitInPayForDecember) {
        return BaseAmountCalculator.calculate(familyComposition, numberOfChildren, familyUnitInPayForDecember);
    }
}