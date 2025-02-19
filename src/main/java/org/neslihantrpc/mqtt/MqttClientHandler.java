package org.neslihantrpc.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MqttClientHandler {

    private final MqttConnectionManager connectionManager;
    private final MessageHandler messageHandler;
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Thread pool for processing
    private final Logger logger = LoggerFactory.getLogger(MqttClientHandler.class);
    public MqttClientHandler(RulesEngineFactory factory) {
        String broker = MqttConfig.current.getBroker();
        String clientId = MqttConfig.current.getTopicId();

        this.connectionManager = new MqttConnectionManager(broker, clientId);
        MqttPublisher publisher = new MqttPublisher(connectionManager);
        MqttMessageProcessor processor = new MqttMessageProcessor(factory, publisher);
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

        String summerInputTopic = MqttConfig.current.getSummerInputTopic();
        String winterInputTopic = MqttConfig.current.getWinterInputTopic();
        connectionManager.getMqttClient().subscribe(summerInputTopic, 1);
        connectionManager.getMqttClient().subscribe(winterInputTopic, 1);

        logger.info("Subscribed to topic: {}", summerInputTopic);
        logger.info("Subscribed to topic: {}", winterInputTopic);
    }

    /**
     * Stops the MQTT client and shuts down the executor service.
     */
    public void stop() {
        executorService.shutdown();
        connectionManager.disconnect();
    }
}