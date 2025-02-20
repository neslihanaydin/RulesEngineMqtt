package org.neslihantrpc.engine;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KieSessionManager {
    private final KieContainer kieContainer;
    private final String sessionName;
    private final Logger logger = LoggerFactory.getLogger(KieSessionManager.class);

    public KieSessionManager(KieContainer kieContainer, String sessionName) {
        this.kieContainer = kieContainer;
        this.sessionName = sessionName;
    }

    public SupplementEligibilityOutput executeRules(SupplementEligibilityInput input) {
        KieSession kieSession = kieContainer.newKieSession(sessionName);
        try {
            kieSession.getObjects().forEach(obj -> kieSession.delete(kieSession.getFactHandle(obj)));
            kieSession.insert(input);
            kieSession.fireAllRules();
            return kieSession.getObjects().stream()
                    .filter(obj -> obj instanceof SupplementEligibilityOutput)
                    .peek(obj -> logger.info("Supplement Eligibility Output is -> \n{}", obj))
                    .map(obj -> (SupplementEligibilityOutput) obj)
                    .findFirst()
                    .orElseThrow(() -> new NullPointerException("No SupplementEligibilityOutput found"));
        } catch (Exception e ) {
            throw new NullPointerException("Error processing rules: " + e.getMessage());
        } finally {
            kieSession.dispose();
        }
    }
}