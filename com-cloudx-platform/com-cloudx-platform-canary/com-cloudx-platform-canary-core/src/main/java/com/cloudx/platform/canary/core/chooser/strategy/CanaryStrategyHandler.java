package com.cloudx.platform.canary.core.chooser.strategy;

import com.cloudx.platform.canary.core.rule.CanaryRule;

/**
 * 规则匹配
 */
public abstract class CanaryStrategyHandler {

    protected CanaryStrategyHandler next;

    public CanaryStrategyHandler setNext(CanaryStrategyHandler next) {
        this.next = next;
        return next;
    }

    public boolean match(CanaryRule rule, String canaryValue, String requestIp) {
        if (doMatch(rule, canaryValue, requestIp)) {
            return true;
        }
        return next != null && next.match(rule, canaryValue, requestIp);
    }

    protected abstract boolean doMatch(CanaryRule rule, String canaryValue, String requestIp);
}