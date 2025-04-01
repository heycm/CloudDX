package com.cloudx.platform.canary.core.chooser.strategy;

import com.cloudx.platform.canary.core.rule.CanaryRule;
import com.cloudx.platform.canary.core.rule.CanaryStrategy;

public class FlowStrategyHandler extends CanaryStrategyHandler {

    @Override
    protected boolean doMatch(CanaryRule rule, String canaryValue, String requestIp) {
        return rule.getStrategy() == CanaryStrategy.FLOW;
    }
}