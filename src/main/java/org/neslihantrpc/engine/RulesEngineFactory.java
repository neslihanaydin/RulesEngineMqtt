package org.neslihantrpc.engine;

import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.neslihantrpc.enums.Supplement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RulesEngineFactory {
    private final Logger logger = LoggerFactory.getLogger(RulesEngineFactory.class);
    private final KieContainer winterKieContainer;
    private final KieContainer summerKieContainer;


    public RulesEngineFactory(String ruleFilePath) {
        this.winterKieContainer = buildKieContainer(ruleFilePath + "winter/");
        this.summerKieContainer = buildKieContainer(ruleFilePath + "summer/");
    }

    private KieContainer buildKieContainer(String ruleFilePath) {
        try {
            KieServices kieServices = KieServices.Factory.get();
            KieFileSystem kieFileSystem = kieServices.newKieFileSystem();

            kieFileSystem.write(ResourceFactory.newFileResource(ruleFilePath + "single_rules.drl"));
            kieFileSystem.write(ResourceFactory.newFileResource(ruleFilePath + "family_rules.drl"));
            kieFileSystem.write(ResourceFactory.newFileResource(ruleFilePath + "ineligibility_rules.drl"));

            KieBuilder kieBuilder = kieServices.newKieBuilder(kieFileSystem);
            kieBuilder.buildAll();

            return kieServices.newKieContainer(kieServices.getRepository().getDefaultReleaseId());
        } catch (Exception e) {
            logger.error("Failed to build KieBase from rules file at path: {}, Error: {}", ruleFilePath, e.getMessage());
            throw new RuntimeException("Error building KieBase from rules file: " + e.getMessage());
        }
    }

    public RulesEngine create(Supplement supplement) {
        try {
            switch (supplement) {
                case WINTER: return new WinterRulesEngine(winterKieContainer);
                case SUMMER: return new SummerRulesEngine(summerKieContainer);
                default: throw new IllegalArgumentException("Unknown supplement type: " + supplement);
            }
        } catch (Exception e) {
            logger.error("Error creating RulesEngine: {}", e.getMessage());
            throw new RuntimeException("Error creating RulesEngine: " + e.getMessage());
        }
    }
}