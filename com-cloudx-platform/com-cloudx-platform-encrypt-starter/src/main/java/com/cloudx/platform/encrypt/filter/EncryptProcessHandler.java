package com.cloudx.platform.encrypt.filter;

import com.cloudx.common.entity.error.ServiceException;
import com.cloudx.common.tools.cipher.AESUtil;
import com.cloudx.platform.encrypt.properties.EncryptProperties.SecretKey;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 加解密处理器
 * @author heycm
 * @version 1.0
 * @since 2025/4/25 22:30
 */
public class EncryptProcessHandler {

    /**
     * 请求解密
     * @param request 请求
     * @return 解密后的请求
     */
    public ServerHttpRequest decryptRequest(ServerHttpRequest request, SecretKey secretKey) {
        // GET 请求，解密请求参数
        if (request.getMethod() == HttpMethod.GET) {
            return decryptQueryParams(request, secretKey);
        }
        // POST 请求，解密请求体
        return decryptRequestBody(request, secretKey);
    }

    private ServerHttpRequest decryptQueryParams(ServerHttpRequest request, SecretKey secretKey) {
        MultiValueMap<String, String> params = request.getQueryParams();
        MultiValueMap<String, String> decryptedParams = new LinkedMultiValueMap<>();
        params.forEach((key, values) -> values.forEach(
                value -> decryptedParams.add(key, AESUtil.decrypt(value, secretKey.getSecretKey(), secretKey.getSecretIv()))));

        URI uri = UriComponentsBuilder.fromUri(request.getURI()).replaceQueryParams(decryptedParams).build().toUri();
        return request.mutate().uri(uri).build();
    }

    private ServerHttpRequest decryptRequestBody(ServerHttpRequest request, SecretKey secretKey) {
        return new ServerHttpRequestDecorator(request) {
            @Override
            public Flux<DataBuffer> getBody() {
                return DataBufferUtils.join(super.getBody()).flatMapMany(joinedBuffer -> {
                    try {
                        // 读原请求体
                        byte[] rawBytes = new byte[joinedBuffer.readableByteCount()];
                        joinedBuffer.read(rawBytes);
                        String rawJson = new String(rawBytes, StandardCharsets.UTF_8);
                        // 解密请求体
                        String decrypted = AESUtil.decrypt(rawJson, secretKey.getSecretKey(), secretKey.getSecretIv());
                        return Flux.just(new DefaultDataBufferFactory().wrap(decrypted.getBytes(StandardCharsets.UTF_8)));
                    } catch (Exception e) {
                        return Mono.error(new ServiceException("请求解密失败", e));
                    } finally {
                        DataBufferUtils.release(joinedBuffer);
                    }
                });
            }
        };
    }

    /**
     * 响应加密
     * @param response 响应
     * @return 加密后的响应
     */
    public ServerHttpResponse encryptResponse(ServerHttpResponse response, SecretKey secretKey) {
        return new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                return super.writeWith(DataBufferUtils.join(fluxBody).flatMap(joinedBuffer -> {
                    try {
                        // 读原响应体
                        byte[] rawBytes = new byte[joinedBuffer.readableByteCount()];
                        joinedBuffer.read(rawBytes);
                        String rawJson = new String(rawBytes, StandardCharsets.UTF_8);
                        // 加密返回
                        String encrypted = AESUtil.encrypt(rawJson, secretKey.getSecretKey(), secretKey.getSecretIv());
                        return Mono.just(response.bufferFactory().wrap(encrypted.getBytes(StandardCharsets.UTF_8)));
                    } catch (Exception e) {
                        return Mono.error(new ServiceException("响应加密失败", e));
                    } finally {
                        DataBufferUtils.release(joinedBuffer);
                    }
                }));
            }
        };
    }
}
