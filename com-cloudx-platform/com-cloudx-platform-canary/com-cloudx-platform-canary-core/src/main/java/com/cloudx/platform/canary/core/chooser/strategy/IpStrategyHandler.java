package com.cloudx.platform.canary.core.chooser.strategy;

import com.cloudx.platform.canary.core.rule.CanaryRule;
import com.cloudx.platform.canary.core.rule.CanaryStrategy;
import com.cloudx.platform.canary.core.utils.CanaryHelper;

import java.util.Set;

public class IpStrategyHandler extends CanaryStrategyHandler {

    @Override
    protected boolean doMatch(CanaryRule rule, String canaryValue, String requestIp) {
        if (rule.getStrategy() != CanaryStrategy.IP) {
            return false;
        }
        Set<String> values = Set.of(rule.getCanaryValue().split(","));
        for (String cidr : values) {
            if (CanaryHelper.isIpRange(cidr, requestIp)) {
                return true;
            }
        }
        return false;
    }
}