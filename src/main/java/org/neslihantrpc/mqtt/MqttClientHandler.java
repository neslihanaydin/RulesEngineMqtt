package org.neslihantrpc.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.neslihantrpc.engine.RulesEngine;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.neslihantrpc.enums.Supplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MqttClientHandler {

    private final MqttConnectionManager connectionManager;
    private final MessageHandler messageHandler;
    private final String inputTopic;
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Thread pool for processing
    private final Logger logger = LoggerFactory.getLogger(MqttClientHandler.class);
    public MqttClientHandler(RulesEngineFactory factory) {
        String broker = MqttConfig.current.getBroker();
        String clientId = MqttConfig.current.getTopicId();
        String appType = MqttConfig.current.getAppType();
        String outputTopic;
        RulesEngine rulesEngine;

        if ("WINTER".equals(appType)) {
            logger.info("Winter app type selected.");
            inputTopic = MqttConfig.current.getWinterInputTopic();
            outputTopic = MqttConfig.current.getWinterOutputTopic();
            rulesEngine = factory.create(Supplement.WINTER);
        } else if ("SUMMER".equals(appType)) {
            logger.info("Summer app type selected.");
            inputTopic = MqttConfig.current.getSummerInputTopic();
            outputTopic = MqttConfig.current.getSummerOutputTopic();
            rulesEngine = factory.create(Supplement.SUMMER);
        } else {
            throw new IllegalArgumentException("Unknown app type: " + appType);
        }

        this.connectionManager = new MqttConnectionManager(broker, clientId);
        MqttPublisher publisher = new MqttPublisher(connectionManager);
        MqttMessageProcessor processor = new MqttMessageProcessor(rulesEngine, publisher, outputTopic, appType);
        this.messageHandler = new MessageHandler(processor);
    }

    public void start() throws MqttException {
        connectionManager.connect();
        connectionManager.getMqttClient().setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {
                logger.error("Connection lost", throwable);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                executorService.submit(() -> messageHandler.handleMessage(topic, mqttMessage));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                logger.info("Message delivered");
            }
        });

        connectionManager.getMqttClient().subscribe(inputTopic, 1);
        logger.info("Subscribed to topic: {}", inputTopic);
    }

    /**
     * Stops the MQTT client and shuts down the executor service.
     */
    public void stop() {
        executorService.shutdown();
        connectionManager.disconnect();
    }
}