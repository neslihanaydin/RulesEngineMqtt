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
        RulesEngineFactory rulesEngineFactory = new RulesEngineFactory();
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
}