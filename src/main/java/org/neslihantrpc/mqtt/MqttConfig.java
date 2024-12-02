package org.neslihantrpc.mqtt;

import org.neslihantrpc.enums.ConfigType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MqttConfig {
    public static final MqttConfig current = new MqttConfig();
    private final Logger logger = LoggerFactory.getLogger(MqttConfig.class);
    private final String PROD_PROPERTIES_FILE_PATH = "/prod.properties";
    private final String TEST_PROPERTIES_FILE_PATH = "/test.properties";
    private final Properties properties = new Properties();

    private MqttConfig() {
        setEnvironment(ConfigType.PROD);
    }

    public void setEnvironment(ConfigType configType) {
        if (configType == ConfigType.PROD) {
            loadProperties(PROD_PROPERTIES_FILE_PATH);
        } else {
            loadProperties(TEST_PROPERTIES_FILE_PATH);
        }
    }
    private void loadProperties(String propertiesFilePath) {
        try (InputStream inputStream = MqttConfig.class.getResourceAsStream(propertiesFilePath)) {
            if (inputStream != null) {
                properties.load(inputStream);
                logger.info("Configuration file loaded successfully.");
            } else {
                throw new IOException("Property file not found in classpath: " + propertiesFilePath);
            }
        } catch (IOException ex) {
            logger.error("Error loading configuration file: {}", ex.getMessage());
        }
    }

    private String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getBroker() {
        return getProperty("mqtt.broker");
    }

    public String getTopicId() {
        return getProperty("mqtt.topic.id");
    }

    public String getInputTopic() {
        return getProperty("mqtt.input.topic") + getTopicId();
    }

    public String getOutputTopic() {
        return getProperty("mqtt.output.topic") + getTopicId();
    }

    public String getRulesFilePath() {
        return getProperty("rules.file.path");
    }
}