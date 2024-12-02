package org.neslihantrpc.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.neslihantrpc.util.JsonHandler;

public class WinterSupplementEligibilityOutput {

    private String id; // ID From InputPerson
    private Boolean isEligible; // Equal to "familyUnitInPayForDecember"
    private Float baseAmount; // Base amount calculated from family composition
    private Float childrenAmount; // Additional amount for children
    private Float supplementAmount; // Total amount

    public WinterSupplementEligibilityOutput() { }
    public WinterSupplementEligibilityOutput(String id, Boolean isEligible, Float baseAmount, Float childrenAmount, Float supplementAmount) {
        this.id = id;
        this.isEligible = isEligible;
        this.baseAmount = baseAmount;
        this.childrenAmount = childrenAmount;
        this.supplementAmount = supplementAmount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getEligible() {
        return isEligible;
    }

    public void setEligible(Boolean eligible) {
        isEligible = eligible;
    }

    public Float getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Float baseAmount) {
        this.baseAmount = baseAmount;
        setSupplementAmount();
    }

    public Float getChildrenAmount() {
        return childrenAmount;
    }

    public void setChildrenAmount(Integer numberOfChildren) {
        if (isEligible) {
            this.childrenAmount = numberOfChildren * 20F;
        } else {
            this.childrenAmount = 0F;
        }
    }

    private void setSupplementAmount() {
        this.supplementAmount = baseAmount + childrenAmount;
    }

    public Float getSupplementAmount() {
        return supplementAmount;
    }

    @Override
    public String toString() {
        String str = "WinterSupplementEligibilityOutput{" +
                "id='" + id + '\'' +
                ", isEligible=" + isEligible +
                ", baseAmount=" + baseAmount +
                ", childrenAmount=" + childrenAmount +
                ", supplementAmount=" + supplementAmount +
                '}';
        return str;
    }
    public MqttMessage getJson() {
        String message = JsonHandler.toJson(this);
        MqttMessage outputMessage = new MqttMessage(message.getBytes());
        outputMessage.setQos(1);
        return outputMessage;
    }
}