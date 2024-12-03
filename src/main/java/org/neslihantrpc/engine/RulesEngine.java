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

    /**
     * Processes the given input object to determine Winter Supplement eligibility and calculates the corresponding output.
     *
     * @param person a {@link WinterSupplementEligibilityInput} object containing input data for the rules engine.
     * @return a {@link WinterSupplementEligibilityOutput} object with calculated eligibility and supplement details.
     * Returns null if no valid output is generated.
     * @throws NullPointerException if an error occurs while processing rules.
     */
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