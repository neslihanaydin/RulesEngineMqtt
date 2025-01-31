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

    /**
     * Sets the environment configuration by loading the corresponding properties file.
     *
     * @param configType the environment type (e.g., PROD or TEST)
     */
    public void setEnvironment(ConfigType configType) {
        if (configType == ConfigType.PROD) {
            loadProperties(PROD_PROPERTIES_FILE_PATH);
        } else {
            loadProperties(TEST_PROPERTIES_FILE_PATH);
        }
    }

    /**
     * Loads the properties from the specified file path.
     *
     * @param propertiesFilePath the path to the properties file (e.g., "/prod.properties")
     */
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

    /**
     * Retrieves the value of a property by its key.
     *
     * @param key the key of the property (e.g., "mqtt.broker")
     * @return the value associated with the property key, or null if not found
     */
    private String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getBroker() {
        return getProperty("mqtt.broker");
    }

    public String getTopicId() {
        return getProperty("mqtt.topic.id");
    }

    public String getWinterInputTopic() {
        return getProperty("mqtt.winter.input.topic") + getTopicId();
    }

    public String getWinterOutputTopic() {
        return getProperty("mqtt.winter.output.topic") + getTopicId();
    }

    public String getSummerInputTopic() {
        return getProperty("mqtt.summer.input.topic") + getTopicId();
    }

    public String getSummerOutputTopic() {
        return getProperty("mqtt.summer.output.topic") + getTopicId();
    }

    public String getRulesFilePath() {
        return getProperty("rules.file.path");
    }

    public String getAppType() {
        return getProperty("app.type");
    }
}