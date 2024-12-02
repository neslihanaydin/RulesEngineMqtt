package org.neslihantrpc.engine;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.io.Resource;
import org.kie.internal.utils.KieHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;

public class RulesEngineFactory {
    private final Logger logger = LoggerFactory.getLogger(RulesEngineFactory.class);
    private final KieBase kieBase;

    public RulesEngineFactory(String ruleFilePath) {
        try {
            File file = new File(ruleFilePath);
            if (!file.exists() || !file.isFile()) {
                logger.error("Rule file does not exist or is not a valid file: {}", ruleFilePath);
                throw new RuntimeException("Invalid rule file path: " + ruleFilePath);
            }
            KieHelper kieHelper = new KieHelper();
            Resource resource = KieServices.Factory.get().getResources().newFileSystemResource(file);
            kieHelper.addResource(resource);
            this.kieBase = kieHelper.build();
        } catch (Exception e) {
            logger.error("Failed to build KieBase from rules file at path: {}, Error: {}", ruleFilePath, e.getMessage());
            throw new RuntimeException("Error building KieBase from rules file: " + e.getMessage());
        }
    }

    public RulesEngine create() {
        try {
            logger.info("Creating a new RulesEngine instance.");
            return new RulesEngine(kieBase.newKieSession());
        } catch (Exception e) {
            logger.error("Error creating RulesEngine: {}", e.getMessage());
            throw new RuntimeException("Error creating RulesEngine: " + e.getMessage());
        }
    }
}
