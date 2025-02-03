package org.neslihantrpc.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.util.JsonHandler;


public class WinterSupplementEligibilityInput extends SupplementEligibilityInput{

    private Boolean familyUnitInPayForDecember;

    public WinterSupplementEligibilityInput() {
    }

    public WinterSupplementEligibilityInput(
            String id,
            Integer numberOfChildren,
            FamilyComposition familyComposition,
            Boolean familyUnitInPayForDecember) {
        super(id, numberOfChildren, familyComposition);
        this.familyUnitInPayForDecember = familyUnitInPayForDecember;
    }

    public Boolean getFamilyUnitInPayForDecember() {
        return familyUnitInPayForDecember;
    }

    @Override
    public String toString() {
        String str = "WinterSupplementEligibilityInput{" +
                "id='" + super.getId() + '\'' +
                ", numberOfChildren=" + super.getNumberOfChildren() +
                ", familyComposition='" + super.getFamilyComposition() + '\'' +
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