package org.neslihantrpc.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.util.JsonHandler;

public class SummerSupplementEligibilityInput extends SupplementEligibilityInput {
    private Boolean familyUnitInPayForJuly;
    private Float householdIncome;

    public SummerSupplementEligibilityInput(String id,
                                            Integer numberOfChildren,
                                            FamilyComposition familyComposition,
                                            Float householdIncome,
                                            Boolean familyUnitInPayForJuly) {
        super(id, numberOfChildren, familyComposition);
        if (householdIncome < 0) {
            throw new IllegalArgumentException("Household income cannot be less than 0.");
        }
        this.familyUnitInPayForJuly = familyUnitInPayForJuly;
        this.householdIncome = householdIncome;
    }

    public Float getHouseholdIncome() {
        return householdIncome;
    }

    public Boolean getFamilyUnitInPayForJuly() {
        return familyUnitInPayForJuly;
    }

    @Override
    public String toString() {
        String str = "SummerSupplementEligibilityInput{" +
                "id='" + super.getId() + '\'' +
                ", numberOfChildren=" + super.getNumberOfChildren() +
                ", familyComposition='" + super.getFamilyComposition() + '\'' +
                ", householdIncome='" + householdIncome + '\'' +
                ", familyUnitInPayForJuly=" + familyUnitInPayForJuly +
                '}';
        return str;
    }

    public MqttMessage getJson() {
        try {
            String message = JsonHandler.toJson(this);
            MqttMessage inputMessage = new MqttMessage(message.getBytes());
            inputMessage.setQos(1);
            return inputMessage;
        } catch (Exception e) {
            throw new RuntimeException("JSON convert error", e);
        }
    }
}