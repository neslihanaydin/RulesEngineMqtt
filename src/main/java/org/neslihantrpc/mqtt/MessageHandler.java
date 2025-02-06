package org.neslihantrpc.mqtt;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageHandler {
    private final MqttMessageProcessor processor;
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    public MessageHandler(MqttMessageProcessor processor) {
        this.processor = processor;
    }

    public void handleMessage(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        logger.info("Received message on topic {}: {}", topic, payload);
        processor.processMessage(topic, payload);
    }
}