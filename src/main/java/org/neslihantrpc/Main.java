package org.neslihantrpc;

import org.eclipse.paho.client.mqttv3.*;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.neslihantrpc.mqtt.MqttClientHandler;
import org.neslihantrpc.mqtt.MqttConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        RulesEngineFactory rulesEngineFactory = new RulesEngineFactory();
        MqttClientHandler mqttClientHandler = new MqttClientHandler(rulesEngineFactory);
        try {
            mqttClientHandler.start();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                mqttClientHandler.stop();
                logger.info("Shutdown hook triggered. MQTT client stopped.");
            }));
        } catch (MqttException e) {
            logger.error("Error starting the application: {}", e.getMessage(), e);
            mqttClientHandler.stop();
        }
    }
}