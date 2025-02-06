package org.neslihantrpc.mqtt;

import org.neslihantrpc.engine.RulesEngine;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.util.JsonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttMessageProcessor {
    private final RulesEngine rulesEngine;
    private final MqttPublisher publisher;
    private final String outputTopic;
    private final String appType;
    private static final Logger logger = LoggerFactory.getLogger(MqttMessageProcessor.class);

    public MqttMessageProcessor(RulesEngine rulesEngine, MqttPublisher publisher, String outputTopic, String appType) {
        this.rulesEngine = rulesEngine;
        this.publisher = publisher;
        this.outputTopic = outputTopic;
        this.appType = appType;
    }

    public void processMessage(String topic, String payload) {
        try {
            logger.info("Processing message from topic {}: {}", topic, payload);
            SupplementEligibilityInput input;
            if (appType.equals("WINTER")) {
                input = JsonHandler.fromJson(payload, WinterSupplementEligibilityInput.class);
            } else if (appType.equals("SUMMER")) {
                input = JsonHandler.fromJson(payload, SummerSupplementEligibilityInput.class);
            } else {
                throw new IllegalArgumentException("Unknown app type: " + appType);
            }
            logger.info("Processing input" + input.toString());

            SupplementEligibilityOutput output = rulesEngine.process(input);
            if (output != null) {
                publisher.publish(outputTopic, JsonHandler.toJson(output));
            }
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
        }
    }
}