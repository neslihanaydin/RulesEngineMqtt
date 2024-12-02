package org.neslihantrpc.engine;

import org.kie.api.runtime.KieSession;
import org.neslihantrpc.model.WinterSupplementEligibilityInput;
import org.neslihantrpc.model.WinterSupplementEligibilityOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesEngine {
    private final Logger logger = LoggerFactory.getLogger(RulesEngine.class);
    private final KieSession kieSession;

    public RulesEngine(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    public WinterSupplementEligibilityOutput process(WinterSupplementEligibilityInput person) {

        try {
            kieSession.getObjects().forEach(obj -> kieSession.delete(kieSession.getFactHandle(obj)));
            kieSession.insert(person);
            kieSession.fireAllRules();

            return kieSession.getObjects().stream()
                    .filter(obj -> obj instanceof WinterSupplementEligibilityOutput)
                    .peek(obj -> logger.info("WinterSupplementEligibilityOutput is -> {}", obj))
                    .map(obj -> (WinterSupplementEligibilityOutput) obj)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e ) {
            throw new NullPointerException("Error processing rules: " + e.getMessage());
        } finally {
            kieSession.dispose();
        }
    }
}