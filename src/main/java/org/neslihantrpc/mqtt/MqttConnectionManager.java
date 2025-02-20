package org.neslihantrpc.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttConnectionManager {
    private MqttClient mqttClient;
    private final String broker;
    private final String clientId;
    private static final Logger logger = LoggerFactory.getLogger(MqttConnectionManager.class);

    public MqttConnectionManager(String broker, String clientId) {
        this.broker = broker;
        this.clientId = clientId;
    }

    public void connect() throws MqttException {
        mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);

        try {
            mqttClient.connect(options);
            logger.info("Connected to MQTT broker.");
        } catch (MqttException e) {
            logger.error("MQTT Connection failed: {}", e.getMessage(), e);
            throw e;
        }
    }

    public void disconnect() {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
                mqttClient.close();
                logger.info("Disconnected from MQTT broker.");
            } catch (MqttException e) {
                logger.error("Error while disconnecting: {}", e.getMessage(), e);
            }
        }
    }

    public boolean isConnected() {
        return mqttClient != null && mqttClient.isConnected();
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }
}