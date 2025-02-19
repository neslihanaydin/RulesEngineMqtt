package org.neslihantrpc.mqtt;

import org.neslihantrpc.engine.RulesEngine;
import org.neslihantrpc.engine.RulesEngineFactory;
import org.neslihantrpc.enums.Supplement;
import org.neslihantrpc.model.SummerSupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.util.JsonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttMessageProcessor {

    private final RulesEngine winterRulesEngine;
    private final RulesEngine summerRulesEngine;
    private final MqttPublisher publisher;
    private static final Logger logger = LoggerFactory.getLogger(MqttMessageProcessor.class);

    public MqttMessageProcessor(RulesEngineFactory factory, MqttPublisher publisher) {
        this.winterRulesEngine = factory.create(Supplement.WINTER);
        this.summerRulesEngine = factory.create(Supplement.SUMMER);
        this.publisher = publisher;
    }

    public void processMessage(String topic, String payload) {
        try {
            logger.info("Processing message from topic {}: {}", topic, payload);
            SupplementEligibilityInput input;
            RulesEngine rulesEngine;
            String outputTopic;

            if (topic.contains("Winter")) {
                input = JsonHandler.fromJson(payload, WinterSupplementEligibilityInput.class);
                rulesEngine = winterRulesEngine;
                outputTopic = "BRE/calculateWinterSupplementOutput/af3054e0-27ff-428e-9531-72b88106039c";
            } else if (topic.contains("Summer")) {
                input = JsonHandler.fromJson(payload, SummerSupplementEligibilityInput.class);
                rulesEngine = summerRulesEngine;
                outputTopic = "BRE/calculateSummerSupplementOutput/af3054e0-27ff-428e-9531-72b88106039c";
            } else {
                throw new IllegalArgumentException("Unknown app type: " + topic);
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