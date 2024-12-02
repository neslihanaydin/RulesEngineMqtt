package org.neslihantrpc.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.util.JsonHandler;

public class WinterSupplementEligibilityInput {
    private String id; // Unique ID, should be included in the results
    private Integer numberOfChildren;
    private FamilyComposition familyComposition; // Choices are ["Single", "Couple"]
    private Boolean familyUnitInPayForDecember; // Used for eligibility determination

    public WinterSupplementEligibilityInput(String id, Integer numberOfChildren, FamilyComposition familyComposition, Boolean familyUnitInPayForDecember) {
        if (numberOfChildren < 0) {
            throw new IllegalArgumentException("Number of children cannot be less than 0.");
        }
        this.id = id;
        this.numberOfChildren = numberOfChildren;
        this.familyComposition = familyComposition;
        this.familyUnitInPayForDecember = familyUnitInPayForDecember;
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

    public Boolean getFamilyUnitInPayForDecember() {
        return familyUnitInPayForDecember;
    }

    @Override
    public String toString() {
        String str = "WinterSupplementEligibilityInput{" +
                "id='" + id + '\'' +
                ", numberOfChildren=" + numberOfChildren +
                ", familyComposition='" + familyComposition + '\'' +
                ", familyUnitInPayForDecember=" + familyUnitInPayForDecember +
                '}';
        return str;
    }

    public MqttMessage getJson() {
        String message = JsonHandler.toJson(this);
        MqttMessage inputMessage = new MqttMessage(message.getBytes());
        inputMessage.setQos(1);
        return inputMessage;
    }
}