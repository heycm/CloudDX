package cn.heycm.platform.canary.core.chooser.strategy;

import cn.heycm.platform.canary.core.rule.CanaryRule;
import cn.heycm.platform.canary.core.rule.CanaryStrategy;
import java.util.Set;

public class HeadStrategyHandler extends CanaryStrategyHandler {

    @Override
    protected boolean doMatch(CanaryRule rule, String canaryValue, String requestIp) {
        if (rule.getStrategy() != CanaryStrategy.HEAD) {
            return false;
        }
        Set<String> values = Set.of(rule.getCanaryValue().split(","));
        return values.contains(canaryValue);
    }
}