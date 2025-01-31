package org.neslihantrpc;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.neslihantrpc.enums.ConfigType;
import org.neslihantrpc.enums.FamilyComposition;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.mqtt.MqttClientHandler;
import org.neslihantrpc.mqtt.MqttConfig;

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

    @Test
    void testSuccessReceivedMessage() {
        try {
            mqttClientHandler.start();
            MqttClient mqttClient = mqttClientHandler.getMqttClient();
            WinterSupplementEligibilityInput input = new WinterSupplementEligibilityInput("a0c5365f", 2, FamilyComposition.SINGLE, true);
            mqttClient.publish(MqttConfig.current.getInputTopic(), input.getJson());

            mqttClient.subscribe(MqttConfig.current.getOutputTopic(), 1, new IMqttMessageListener() {
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
}