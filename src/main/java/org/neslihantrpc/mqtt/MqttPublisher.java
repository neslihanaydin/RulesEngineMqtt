package org.neslihantrpc.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttPublisher {
    private final MqttConnectionManager connectionManager;
    private static final Logger logger = LoggerFactory.getLogger(MqttPublisher.class);

    public MqttPublisher(MqttConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void publish(String topic, String payload) {
        try {
            // System.out.println("Payload is : " + payload);
            MqttMessage message = new MqttMessage(payload.getBytes());
            message.setQos(1);
            connectionManager.getMqttClient().publish(topic, message);
            logger.info("Output published to topic: {} : {}", topic, payload);
        } catch (MqttException e) {
            logger.error("Failed to publish message: {}", e.getMessage(), e);
        }
    }
}