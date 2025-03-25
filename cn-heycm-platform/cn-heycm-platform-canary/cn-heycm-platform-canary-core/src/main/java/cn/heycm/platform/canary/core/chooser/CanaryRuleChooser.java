package cn.heycm.platform.canary.core.chooser;

import cn.heycm.common.tools.Jackson;
import cn.heycm.platform.canary.core.cache.CanaryRuleCache;
import cn.heycm.platform.canary.core.chooser.strategy.CanaryStrategyHandler;
import cn.heycm.platform.canary.core.chooser.strategy.FlowStrategyHandler;
import cn.heycm.platform.canary.core.chooser.strategy.HeadStrategyHandler;
import cn.heycm.platform.canary.core.chooser.strategy.IpStrategyHandler;
import cn.heycm.platform.canary.core.request.CanaryRequest;
import cn.heycm.platform.canary.core.rule.CanaryRule;
import cn.heycm.platform.canary.core.rule.CanaryStrategy;
import cn.heycm.platform.canary.core.utils.CanaryHelper;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;

/**
 * 根据灰度规则选择灰度服务实例
 * @author heycm
 * @version 1.0
 * @since 2025/3/4 14:24
 */
@Slf4j
public class CanaryRuleChooser {

    public static final String CANARY_TAG_HEADER = "X-Canary-Tag";

    private static final String CANARY_INSTANCE_METADATA = "canary.enable";
    private static final String TAG_CANARY_INSTANCE_METADATA = "canary.tag";

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final CanaryStrategyHandler strategyChain;

    static {
        // 构建策略责任链
        strategyChain = new HeadStrategyHandler();
        strategyChain.setNext(new IpStrategyHandler()).setNext(new FlowStrategyHandler());
    }

    /**
     * 根据灰度规则选择灰度服务实例，并设置相关灰度请求头，无适配灰度时返回所有实例
     * @param serviceId 服务ID
     * @param request   当前请求
     * @param instances 当前服务ID的所有实例列表
     * @return 实例列表
     */
    @SuppressWarnings("rawtypes")
    public static List<ServiceInstance> choose(String serviceId, Request request, List<ServiceInstance> instances) {

        // 请求上下文
        if (!(request instanceof CanaryRequest<?>)) {
            return instances;
        }
        CanaryRequest canaryRequest = (CanaryRequest) request;
        RequestData context = ((RequestDataContext) canaryRequest.getContext()).getClientRequest();

        // 1. 已标记灰度流量，直接转发灰度服务
        if (context.getHeaders().containsKey(CANARY_TAG_HEADER)) {
            return chooseCanaryTag(context, instances);
        }

        // 2. 没有启用灰度配置
        if (!CanaryRuleCache.isEnabled(serviceId)) {
            return instances;
        }

        // 3. 获取灰度服务实例
        List<ServiceInstance> canary = getCanaryInstances(instances);
        if (CollectionUtils.isEmpty(canary)) {
            return instances;
        }

        // 4. 根据规则选择灰度服务实例
        canary = chooseCanaryInstances(serviceId, canaryRequest, context, canary);
        return CollectionUtils.isEmpty(canary) ? instances : canary;
    }

    /**
     * 根据灰度标签选择灰度服务，如果没有灰度服务，则直接转发所有服务实例
     * @param context   请求上下文
     * @param instances 服务实例列表
     * @return 实例列表
     */
    private static List<ServiceInstance> chooseCanaryTag(RequestData context, List<ServiceInstance> instances) {
        // 请求头 X-Canary-Tag 中携带的灰度标签
        String tag = Objects.requireNonNull(context.getHeaders().get(CANARY_TAG_HEADER)).getFirst();
        List<ServiceInstance> canary = instances.stream().filter(instance -> {
            Map<String, String> metadata = instance.getMetadata();
            if (metadata == null) {
                return false;
            }
            // 匹配灰度服务  canary.enable:true && canary.tag:xxx
            return Boolean.parseBoolean(metadata.get(CANARY_INSTANCE_METADATA)) && tag.equals(metadata.get(TAG_CANARY_INSTANCE_METADATA));
        }).toList();
        if (canary.isEmpty()) {
            if (log.isDebugEnabled()) {
                String path = context.getUrl().getPath();
                log.debug("[{}:{}] 没有找到灰度服务实例, 不做灰度流量分发, Path: {}", CANARY_TAG_HEADER, tag, path);
            }
            return instances;
        }
        return canary;
    }

    /**
     * 获取灰度服务实例
     * @param instances 服务实例列表
     * @return 灰度服务实例列表
     */
    private static List<ServiceInstance> getCanaryInstances(List<ServiceInstance> instances) {
        return instances.stream().filter(instance -> {
            Map<String, String> metadata = instance.getMetadata();
            if (metadata == null) {
                return false;
            }
            return Boolean.parseBoolean(metadata.get(CANARY_INSTANCE_METADATA));
        }).toList();
    }

    /**
     * 根据灰度规则选择灰度服务实例
     */
    private static List<ServiceInstance> chooseCanaryInstances(String serviceId, CanaryRequest canaryRequest, RequestData context,
            List<ServiceInstance> canary) {

        // 获取路由匹配规则
        List<CanaryRule> rules = CanaryRuleCache.getRules(serviceId);
        String path = context.getUrl().getPath();
        rules = rules.stream().filter(rule -> rule.getPath().equals(path) || PATH_MATCHER.match(rule.getPath(), path)).toList();
        if (CollectionUtils.isEmpty(rules)) {
            return Collections.emptyList();
        }

        // 请求头中携带的灰度值和请求IP
        String canaryValue = Optional.ofNullable(context.getHeaders().get(CanaryStrategy.HEAD.getHeaderName())).map(List::getFirst)
                .orElse("");
        String requestIp = Optional.ofNullable(context.getHeaders().get(CanaryStrategy.IP.getHeaderName())).map(List::getFirst)
                .orElse(CanaryHelper.getRemoteIpAddress(context.getHeaders()));

        // 使用策略责任链匹配规则
        List<CanaryRule> matchedRules = rules.stream().filter(rule -> strategyChain.match(rule, canaryValue, requestIp))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(matchedRules)) {
            return Collections.emptyList();
        }

        // 获取优先级最高的规则
        CanaryRule rule = getHighestPriorityRule(matchedRules);

        // 标记流量并选择灰度服务实例
        return markAndChooseCanaryInstances(rule, canaryRequest, context, canary);
    }

    /**
     * 获取优先级最高的规则：HEAD > IP > FLOW
     */
    private static CanaryRule getHighestPriorityRule(List<CanaryRule> rules) {
        if (rules.size() == 1) {
            return rules.getFirst();
        }

        Optional<CanaryRule> highestPriority = rules.stream().filter(rule -> rule.getStrategy() == CanaryStrategy.HEAD).findFirst();
        if (highestPriority.isPresent()) {
            return highestPriority.get();
        }

        highestPriority = rules.stream().filter(rule -> rule.getStrategy() == CanaryStrategy.IP).findFirst();

        return highestPriority.orElseGet(rules::getFirst);

    }

    /**
     * 匹配实例Tag，并标记流量
     */
    private static List<ServiceInstance> markAndChooseCanaryInstances(CanaryRule rule, CanaryRequest canaryRequest, RequestData context,
            List<ServiceInstance> canary) {
        List<ServiceInstance> instances = canary.stream().filter(instance -> {
            Map<String, String> metadata = instance.getMetadata();
            return metadata != null && rule.getTag().equals(metadata.get(TAG_CANARY_INSTANCE_METADATA));
        }).toList();

        if (CollectionUtils.isEmpty(instances)) {
            if (log.isDebugEnabled()) {
                String path = context.getUrl().getPath();
                String strategyName = rule.getStrategy().getHeaderName();
                String strategyValue = switch (rule.getStrategy()) {
                    case HEAD -> Optional.ofNullable(context.getHeaders().get(strategyName)).map(List::getFirst).orElse("N/A");
                    case IP -> Optional.ofNullable(context.getHeaders().get(strategyName)).map(List::getFirst)
                            .orElseGet(() -> CanaryHelper.getRemoteIpAddress(context.getHeaders()));
                    case FLOW -> "N/A";
                };
                log.debug("[{}:{}] 没有找到匹配的灰度服务实例, 不做灰度流量分发, Path: {}, Rule: {}", strategyName, strategyValue, path,
                        Jackson.toJson(rule));
            }
            return Collections.emptyList();
        }

        switch (rule.getStrategy()) {
            case HEAD -> {
                canaryRequest.addExtHeader(CANARY_TAG_HEADER, rule.getTag());
                return instances;
            }
            case IP -> {
                String requestIp = Optional.ofNullable(context.getHeaders().get(CanaryStrategy.IP.getHeaderName())).map(List::getFirst)
                        .orElseGet(() -> CanaryHelper.getRemoteIpAddress(context.getHeaders()));
                canaryRequest.addExtHeader(CANARY_TAG_HEADER, rule.getTag());
                canaryRequest.addExtHeader(CanaryStrategy.IP.getHeaderName(), requestIp);
                return instances;
            }
            case FLOW -> {
                if (!CanaryHelper.markFlow(Integer.parseInt(rule.getCanaryValue()))) {
                    return Collections.emptyList();
                }
                canaryRequest.addExtHeader(CANARY_TAG_HEADER, rule.getTag());
                canaryRequest.addExtHeader(CanaryStrategy.FLOW.getHeaderName(), "true");
                return instances;
            }
            default -> {
                return Collections.emptyList();
            }
        }
    }

}
