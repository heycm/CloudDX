package com.cloudx.platform.canary.core.cache;

import com.cloudx.platform.canary.core.rule.CanaryRule;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

/**
 * 灰度规则缓存
 * @author hey
 * @version 1.0
 * @since 2025/3/3 17:35
 */
public class CanaryRuleCache {

    private static final Map<String, List<CanaryRule>> RULES = new ConcurrentHashMap<>();

    public static void refresh(List<CanaryRule> rules) {
        RULES.clear();
        if (CollectionUtils.isEmpty(rules)) {
            return;
        }
        RULES.putAll(rules.stream().filter(CanaryRule::check).collect(Collectors.groupingBy(CanaryRule::getServiceId)));
    }

    public static boolean isEnabled(String serviceId) {
        return !CollectionUtils.isEmpty(RULES.get(serviceId));
    }

    public static List<CanaryRule> getRules(String serviceId) {
        return RULES.getOrDefault(serviceId, Collections.emptyList());
    }

}

