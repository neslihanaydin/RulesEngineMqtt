package org.neslihantrpc.engine;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WinterRulesEngine implements RulesEngine{

    private final KieSessionManager kieSessionManager;
    private final Logger logger = LoggerFactory.getLogger(WinterRulesEngine.class);

    public WinterRulesEngine(KieContainer kieContainer) {
        this.kieSessionManager = new KieSessionManager(kieContainer, "winter-session");
    }

    @Override
    public SupplementEligibilityOutput process(SupplementEligibilityInput input) {
        logger.info("Processing winter rules...");
        return kieSessionManager.executeRules(input);
    }
}
