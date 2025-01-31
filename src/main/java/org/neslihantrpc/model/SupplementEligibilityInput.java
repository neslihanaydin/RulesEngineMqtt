package org.neslihantrpc.model;

import org.neslihantrpc.enums.FamilyComposition;

public class SupplementEligibilityInput {
    private String id;
    private Integer numberOfChildren;
    private FamilyComposition familyComposition;

    public SupplementEligibilityInput() {
    }

    public SupplementEligibilityInput(
            String id,
            Integer numberOfChildren,
            FamilyComposition familyComposition) {
        if (numberOfChildren < 0) {
            throw new IllegalArgumentException("Number of children cannot be less than 0.");
        }
        this.id = id;
        this.numberOfChildren = numberOfChildren;
        this.familyComposition = familyComposition;
    }

    public String getId() {
        return id;
    }

    public Integer getNumberOfChildren() {
        return numberOfChildren;
    }

    public FamilyComposition getFamilyComposition() {
        return familyComposition;
    }
}
