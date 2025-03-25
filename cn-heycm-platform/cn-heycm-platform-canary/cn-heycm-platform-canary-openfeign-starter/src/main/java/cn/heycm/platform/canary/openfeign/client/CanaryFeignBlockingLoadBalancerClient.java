package cn.heycm.platform.canary.openfeign.client;

import static cn.heycm.platform.canary.openfeign.client.LoadBalancerUtils.buildRequestData;
import static cn.heycm.platform.canary.openfeign.client.LoadBalancerUtils.executeWithLoadBalancerLifecycleProcessing;

import cn.heycm.platform.canary.core.request.CanaryRequest;
import feign.Client;
import feign.Request;
import feign.Response;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.CompletionContext;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycle;
import org.springframework.cloud.client.loadbalancer.LoadBalancerLifecycleValidator;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.client.loadbalancer.RequestDataContext;
import org.springframework.cloud.client.loadbalancer.ResponseData;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.LoadBalancerFeignRequestTransformer;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * LoadBalancerClient 灰度实现 {@link org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient}
 * @author heycm
 * @version 1.0
 * @since 2025/3/13 21:52
 */
@Slf4j
public class CanaryFeignBlockingLoadBalancerClient implements Client {

    private final Client delegate;

    private final LoadBalancerClient loadBalancerClient;

    private final LoadBalancerClientFactory loadBalancerClientFactory;

    private final List<LoadBalancerFeignRequestTransformer> transformers;


    public CanaryFeignBlockingLoadBalancerClient(Client delegate, LoadBalancerClient loadBalancerClient,
            LoadBalancerClientFactory loadBalancerClientFactory, List<LoadBalancerFeignRequestTransformer> transformers) {
        this.delegate = delegate;
        this.loadBalancerClient = loadBalancerClient;
        this.loadBalancerClientFactory = loadBalancerClientFactory;
        this.transformers = transformers;
    }


    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        final URI originalUri = URI.create(request.url());
        String serviceId = originalUri.getHost();
        Assert.state(serviceId != null, "Request URI does not contain a valid hostname: " + originalUri);
        String hint = getHint(serviceId);

        // 修改 DefaultRequest 换为 CanaryRequest，以支持在 CanaryRuleChooser 灰度计算时设置扩展请求头
        // DefaultRequest<RequestDataContext> lbRequest = new DefaultRequest<>(new RequestDataContext(buildRequestData(request), hint));
        CanaryRequest<RequestDataContext> lbRequest = new CanaryRequest<>(new RequestDataContext(buildRequestData(request), hint));

        Set<LoadBalancerLifecycle> supportedLifecycleProcessors = LoadBalancerLifecycleValidator.getSupportedLifecycleProcessors(
                loadBalancerClientFactory.getInstances(serviceId, LoadBalancerLifecycle.class), RequestDataContext.class,
                ResponseData.class, ServiceInstance.class);
        supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onStart(lbRequest));
        ServiceInstance instance = loadBalancerClient.choose(serviceId, lbRequest);
        org.springframework.cloud.client.loadbalancer.Response<ServiceInstance> lbResponse = new DefaultResponse(instance);
        if (instance == null) {
            String message = "Load balancer does not contain an instance for the service " + serviceId;
            if (log.isWarnEnabled()) {
                log.warn(message);
            }
            supportedLifecycleProcessors.forEach(lifecycle -> lifecycle.onComplete(
                    new CompletionContext<ResponseData, ServiceInstance, RequestDataContext>(CompletionContext.Status.DISCARD, lbRequest,
                            lbResponse)));
            return Response.builder().request(request).status(HttpStatus.SERVICE_UNAVAILABLE.value()).body(message, StandardCharsets.UTF_8)
                    .build();
        }
        String reconstructedUrl = loadBalancerClient.reconstructURI(instance, originalUri).toString();

        // 设置请求头
        addExtHeader(request, lbRequest);

        Request newRequest = buildRequest(request, reconstructedUrl, instance);
        return executeWithLoadBalancerLifecycleProcessing(delegate, options, newRequest, lbRequest, lbResponse,
                supportedLifecycleProcessors);
    }

    private void addExtHeader(Request request, CanaryRequest<RequestDataContext> lbRequest) {
        if (!CollectionUtils.isEmpty(lbRequest.getExtHeader())) {
            lbRequest.getExtHeader().forEach((name, value) -> request.header(name, value.toString()));
        }
    }

    protected Request buildRequest(Request request, String reconstructedUrl) {
        return Request.create(request.httpMethod(), reconstructedUrl, request.headers(), request.body(), request.charset(),
                request.requestTemplate());
    }

    protected Request buildRequest(Request request, String reconstructedUrl, ServiceInstance instance) {
        Request newRequest = buildRequest(request, reconstructedUrl);
        if (transformers != null) {
            for (LoadBalancerFeignRequestTransformer transformer : transformers) {
                newRequest = transformer.transformRequest(newRequest, instance);
            }
        }
        return newRequest;
    }

    // Visible for Sleuth instrumentation
    public Client getDelegate() {
        return delegate;
    }

    private String getHint(String serviceId) {
        LoadBalancerProperties properties = loadBalancerClientFactory.getProperties(serviceId);
        String defaultHint = properties.getHint().getOrDefault("default", "default");
        String hintPropertyValue = properties.getHint().get(serviceId);
        return hintPropertyValue != null ? hintPropertyValue : defaultHint;
    }
}
