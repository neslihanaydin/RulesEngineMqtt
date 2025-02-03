package org.neslihantrpc.model;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.util.JsonHandler;

public class SupplementEligibilityOutput {

    private String id; // ID From InputPerson
    private Boolean isEligible; // Equal to "familyUnitInPayForDecember" or "familyUnitInPayForJuly"
    private Float childrenAmount; // Additional amount for children
    private Float baseAmount; // Base amount calculated from family composition
    private Float supplementAmount; // Total amount
    private Supplement supplementType; // Type of supplement (WINTER or SUMMER)

    public SupplementEligibilityOutput() { }

    public SupplementEligibilityOutput(String id, Boolean familyUnitInPay, float childrenAmount, float baseAmount, Supplement supplementType) {
        this.id = id;
        this.isEligible = familyUnitInPay;
        this.childrenAmount = childrenAmount;
        this.baseAmount = baseAmount;
        this.supplementType = supplementType;
        setSupplementAmount();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getIsEligible() {
        return isEligible;
    }

    public void setIsEligible(Boolean eligible) {
        isEligible = eligible;
    }

    private void setSupplementAmount() {
        this.supplementAmount = baseAmount + childrenAmount;
    }

    public Float getSupplementAmount() {
        return supplementAmount;
    }

    public Supplement getSupplementType() {
        return supplementType;
    }

    public void setSupplementType(Supplement supplementType) {
        this.supplementType = supplementType;
    }

    @Override
    public String toString() {
        return String.format(
                "{\n" +
                        "  id='%s',\n" +
                        "  isEligible=%s,\n" +
                        "  baseAmount=%.1f,\n" +
                        "  childrenAmount=%.1f,\n" +
                        "  supplementAmount=%.1f\n" +
                        "}", id, isEligible, baseAmount, childrenAmount, supplementAmount
        );
    }

    public MqttMessage getJson() {
        String message = JsonHandler.toJson(this);
        MqttMessage outputMessage = new MqttMessage(message.getBytes());
        outputMessage.setQos(1);
        return outputMessage;
    }
}