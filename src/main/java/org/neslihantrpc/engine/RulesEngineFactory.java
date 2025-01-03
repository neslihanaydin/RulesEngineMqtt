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

    /**
     * Constructs a RulesEngineFactory by loading the rules from a specified file path.
     * It initializes the KieBase by reading the provided rules file.
     *
     * @param ruleFilePath the path to the rules file to be loaded
     * @throws RuntimeException if the rule file is invalid or cannot be loaded
     */
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

    /**
     * Creates and returns a new instance of the RulesEngine.
     * This method initializes a new KieSession using the previously configured KieBase.
     *
     * @return a new RulesEngine instance
     * @throws RuntimeException if there is an error creating the RulesEngine
     */
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