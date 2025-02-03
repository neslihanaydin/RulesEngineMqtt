package org.neslihantrpc.engine;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SummerRulesEngine implements RulesEngine{

    private final KieContainer kieContainer;
    private final Logger logger = LoggerFactory.getLogger(SummerRulesEngine.class);


    public SummerRulesEngine(KieContainer kieContainer) {
        this.kieContainer = kieContainer;
    }
    
    @Override
    public SupplementEligibilityOutput process(SupplementEligibilityInput input) {
        System.out.println("Processing summer rules");
        KieSession kieSession = kieContainer.newKieSession();

        try {
            kieSession.getObjects().forEach(obj -> kieSession.delete(kieSession.getFactHandle(obj)));
            kieSession.insert(input);
            kieSession.fireAllRules();

            return kieSession.getObjects().stream()
                    .filter(obj -> obj instanceof SupplementEligibilityOutput)
                    .peek(obj -> logger.info("Summer SupplementEligibilityOutput is -> {}", obj))
                    .map(obj -> (SupplementEligibilityOutput) obj)
                    .findFirst()
                    .orElse(null);
        } catch (Exception e ) {
            throw new NullPointerException("Error processing rules: " + e.getMessage());
        } finally {
            kieSession.dispose();
        }
    }
}
