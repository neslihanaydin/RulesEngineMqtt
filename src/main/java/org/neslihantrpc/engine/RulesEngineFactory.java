package org.neslihantrpc.engine;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.neslihantrpc.enums.Supplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesEngineFactory {
    private final Logger logger = LoggerFactory.getLogger(RulesEngineFactory.class);
    private final KieContainer kieContainer;


    public RulesEngineFactory() {
        KieServices kieServices = KieServices.Factory.get();
        this.kieContainer = kieServices.newKieClasspathContainer();

    }

    public RulesEngine create(Supplement supplement) {
        try {
            switch (supplement) {
                case WINTER: return new WinterRulesEngine(kieContainer);
                case SUMMER: return new SummerRulesEngine(kieContainer);
                default: throw new IllegalArgumentException("Unknown supplement type: " + supplement);
            }
        } catch (Exception e) {
            logger.error("Error creating RulesEngine: {}", e.getMessage());
            throw new RuntimeException("Error creating RulesEngine: " + e.getMessage());
        }
    }
}