package org.neslihantrpc.engine;

import org.kie.api.runtime.KieContainer;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummerRulesEngine implements RulesEngine{

    private final KieSessionManager kieSessionManager;
    private final Logger logger = LoggerFactory.getLogger(SummerRulesEngine.class);


    public SummerRulesEngine(KieContainer kieContainer) {
        this.kieSessionManager = new KieSessionManager(kieContainer, "summer-session");
    }
    
    @Override
    public SupplementEligibilityOutput process(SupplementEligibilityInput input) {
        logger.info("Processing summer rules");
        return kieSessionManager.executeRules(input);
    }
}
