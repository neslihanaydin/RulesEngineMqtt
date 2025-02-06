package org.neslihantrpc.mqtt;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.neslihantrpc.enums.ConfigType;


import static org.junit.jupiter.api.Assertions.*;

class WSMqttTest {

    MqttClientHandler mqttClientHandler;

    @BeforeEach
    void setup() {
        MqttConfig.current.setEnvironment(ConfigType.TEST);
        RulesEngineFactory rulesEngineFactory = new RulesEngineFactory(MqttConfig.current.getRulesFilePath());
        mqttClientHandler = new MqttClientHandler(rulesEngineFactory);
    }

    @Test
    void testSetEnvironment() {
        assertEquals(MqttConfig.current.getBroker(), "tcp://localhost:1883");
        MqttConfig.current.setEnvironment(ConfigType.PROD);
        assertEquals(MqttConfig.current.getBroker(), "tcp://test.mosquitto.org:1883");
    }

    @Test
    void testMqttClientStart() {
        // TEST CONNECTION
        assertDoesNotThrow(() -> mqttClientHandler.start());
        // PROD CONNECTION
        MqttConfig.current.setEnvironment(ConfigType.PROD);
        assertDoesNotThrow(() -> mqttClientHandler.start());
    }

    /* TODO
    @Test
    void testSuccessReceivedMessage() throws MqttException, InterruptedException {

        WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("a0c5365f", 2, FamilyComposition.SINGLE, true);
        String jsonPayload = JsonHandler.toJson(input);

        MqttConnectionManager connectionManager = new MqttConnectionManager(MqttConfig.current.getBroker(), MqttConfig.current.getTopicId());
        connectionManager.connect();
        MqttPublisher publisher = new MqttPublisher(connectionManager);
        publisher.publish(MqttConfig.current.getWinterInputTopic(), jsonPayload);

        CountDownLatch latch = new CountDownLatch(1);

        connectionManager.getMqttClient().subscribe(MqttConfig.current.getWinterOutputTopic(), 1, (topic, message) -> {
            latch.countDown();
        });

        boolean messageReceived = latch.await(5, TimeUnit.SECONDS);
        assertTrue(messageReceived, "Output message was not received in time.");
        /*
        try {
            mqttClientHandler.start();
            MqttClient mqttClient = mqttClientHandler.getMqttClient();
            WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("a0c5365f", 2, FamilyComposition.SINGLE, true);
            mqttClient.publish(MqttConfig.current.getWinterInputTopic(), input.getJson());

            mqttClient.subscribe(MqttConfig.current.getWinterOutputTopic(), 1, new IMqttMessageListener() {
                @Override
                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                    assertEquals(mqttMessage, input.getJson());
                }
            });
            Thread.sleep(1000);
        } catch (MqttException e) {
            fail("Handler couldn't start " + e.getLocalizedMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
     */
}