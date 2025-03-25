package cn.heycm.platform.canary.core.chooser.strategy;

import cn.heycm.platform.canary.core.rule.CanaryRule;
import cn.heycm.platform.canary.core.rule.CanaryStrategy;

public class FlowStrategyHandler extends CanaryStrategyHandler {

    @Override
    protected boolean doMatch(CanaryRule rule, String canaryValue, String requestIp) {
        return rule.getStrategy() == CanaryStrategy.FLOW;
    }
}