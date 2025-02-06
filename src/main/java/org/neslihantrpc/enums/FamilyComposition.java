package org.neslihantrpc.enums;

public enum FamilyComposition {
    SINGLE,
    MARRIED;
    public static FamilyComposition fromString(String value) {
        for (FamilyComposition fc : values()) {
            if (fc.name().equalsIgnoreCase(value)) {
                return fc;
            }
        }
        throw new IllegalArgumentException("Invalid FamilyComposition value: " + value);
    }
}
