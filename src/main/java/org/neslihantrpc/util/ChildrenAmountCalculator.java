package org.neslihantrpc.util;

import org.neslihantrpc.enums.Supplement;

public class ChildrenAmountCalculator {

    public static float calculate(Integer numberOfChildren, Boolean familyUnitInPay, Supplement supplementType) {
        if (familyUnitInPay == null || numberOfChildren == null) {
            return 0F;
        }
        if (!familyUnitInPay) {
            return 0F;
        }

        return switch (supplementType) {
            case SUMMER -> numberOfChildren * 50F;
            case WINTER -> numberOfChildren * 20F;
            default -> 0F;
        };
    }
}