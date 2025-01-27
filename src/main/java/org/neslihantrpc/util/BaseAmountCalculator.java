package org.neslihantrpc.util;

import org.neslihantrpc.enums.FamilyComposition;

public class BaseAmountCalculator {

    public static float calculate(FamilyComposition familyComposition, int numberOfChildren, boolean isEligible) {
        if (!isEligible) {
            return 0F;
        }

        if (familyComposition == FamilyComposition.MARRIED || numberOfChildren > 0) {
            return 120F;
        }

        if (familyComposition == FamilyComposition.SINGLE) {
            return 60F;
        }
        return 0F;
    }
}