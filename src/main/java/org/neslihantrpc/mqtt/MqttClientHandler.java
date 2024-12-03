package org.neslihantrpc.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.neslihantrpc.engine.RulesEngine;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.model.WinterSupplementEligibilityOutput;
import org.neslihantrpc.util.JsonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MqttClientHandler implements AutoCloseable{

    private MqttClient mqttClient;
    private final String broker;
    private final String clientId;
    private final String inputTopic;
    private final String outputTopic;
    private final RulesEngineFactory factory;
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Thread pool for processing
    private final Logger logger = LoggerFactory.getLogger(MqttClientHandler.class);
    public MqttClientHandler(RulesEngineFactory factory) {
        this.broker = MqttConfig.current.getBroker();
        this.clientId = MqttConfig.current.getTopicId();
        this.inputTopic = MqttConfig.current.getInputTopic();
        this.outputTopic = MqttConfig.current.getOutputTopic();
        this.factory = factory;
    }
    /**
     * Starts the MQTT client, connecting to the broker and subscribing to the input topic.
     *
     * @throws MqttException if an error occurs while connecting to the broker or subscribing.
     */
    public void start() throws MqttException {
        mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);

        try {
            mqttClient.connect(mqttConnectOptions);
            logger.info("Connected to MQTT broker.");
        } catch (MqttException e) {
            logger.error("Initial connection failed: " + e.getMessage(), e);
            throw e;
        }

        mqttClient.setCallback(new MqttCallbackHandler());
        mqttClient.subscribe(inputTopic, 1, this::processMessage);
        logger.info("Subscribed to topic: {}", inputTopic);
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    /**
     * Processes the received MQTT message, uses the {@link RulesEngine} to process the input,
     * and publishes the result to the output topic.
     *
     * @param topic The MQTT topic.
     * @param message The received MQTT message.
     */
    private void processMessage(String topic, MqttMessage message) {
        executorService.submit(() -> {
            try {
                if (!mqttClient.isConnected()) {
                    logger.warn("MQTT Client is not connected, reconnecting...");
                    mqttClient.reconnect();
                    logger.info("Reconnected successfully.");
                }
                String payload = new String(message.getPayload());
                WinterSupplementEligibilityInput input = JsonHandler.fromJson(payload, WinterSupplementEligibilityInput.class);
                logger.info("Processing input");

                RulesEngine rulesEngine = factory.create();
                WinterSupplementEligibilityOutput output = rulesEngine.process(input);

                if (output != null) {
                    mqttClient.publish(outputTopic, output.getJson());
                    logger.info("Output published to topic: {}", outputTopic);
                }
            } catch (Exception e) {
                logger.error("Error processing message: {}", e.getMessage(), e);
                e.printStackTrace();
            }
        });
    }

    /**
     * Stops the MQTT client and shuts down the executor service.
     */
    public void stop() {
        executorService.shutdown();
        if (mqttClient != null) {
            try {
                if (mqttClient.isConnected()) {
                    mqttClient.disconnect();
                    mqttClient.close();
                    logger.info("Disconnected and closed client.");
                }
            } catch (MqttException e) {
                logger.error("Error during shutdown: {}", e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    /**
     * Closes the MQTT client handler by stopping the client and releasing resources.
     *
     * @throws MqttException if an error occurs while closing the client.
     */
    @Override
    public void close() throws MqttException {
        stop();
    }

    private class MqttCallbackHandler implements MqttCallback {
        private static final int MAX_RECONNECT_ATTEMPTS = 12;
        private static final int RECONNECT_DELAY_IN_MS = 5000;
        @Override
        public void connectionLost(Throwable throwable) {
            logger.warn("Connection is lost: {}", throwable.getMessage());
            int attemptCounter = 0;
            try {
                while (!mqttClient.isConnected() && attemptCounter < MAX_RECONNECT_ATTEMPTS) {
                    attemptCounter++;
                    try {
                        logger.info("Trying to reconnect (Attempt {}/{})...", attemptCounter, MAX_RECONNECT_ATTEMPTS);
                        mqttClient.reconnect();
                        if (mqttClient.isConnected()) {
                            logger.info("Reconnected successfully on attempt {}", attemptCounter);
                            break;
                        }
                    } catch (MqttException e) {
                        logger.error("Reconnection attempt failed: " + e.getMessage(), e);
                    }
                    Thread.sleep(RECONNECT_DELAY_IN_MS);
                }
                if (!mqttClient.isConnected()) {
                    logger.error("Failed to reconnect after {}", MAX_RECONNECT_ATTEMPTS);
                }
            } catch (InterruptedException e) {
                logger.error("Reconnection process interrupted: {}", e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }

        @Override
        public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
            logger.info("Received message: {} from topic {}", new String(mqttMessage.getPayload()), topic);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            logger.info("Message delivery complete: {}", token.isComplete());
        }
    }
}