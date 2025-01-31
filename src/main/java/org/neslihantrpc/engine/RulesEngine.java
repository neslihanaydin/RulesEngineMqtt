package org.neslihantrpc.engine;

import org.neslihantrpc.model.SupplementEligibilityInput;
import org.neslihantrpc.model.SupplementEligibilityOutput;


public interface RulesEngine {

    SupplementEligibilityOutput process(SupplementEligibilityInput input);
}