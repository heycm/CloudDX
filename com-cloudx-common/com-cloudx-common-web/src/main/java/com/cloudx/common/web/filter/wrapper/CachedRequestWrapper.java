package com.cloudx.common.web.filter.wrapper;

import io.undertow.servlet.util.IteratorEnumeration;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.util.CollectionUtils;

/**
 * 可重复读请求装饰器
 * @author hey
 * @version 1.0
 * @since 2024/12/5 18:03
 */
public class CachedRequestWrapper extends HttpServletRequestWrapper {

    private byte[] cachedBody;

    private Map<String, String> extHeaders;

    private Map<String, String[]> extParams;

    public CachedRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        ServletInputStream is = request.getInputStream();
        cachedBody = is.readAllBytes();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedServletInputStream(cachedBody);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(cachedBody);
        return new BufferedReader(new InputStreamReader(is));
    }

    public void setBody(byte[] body) {
        cachedBody = body;
    }

    public byte[] getContentAsByteArray() {
        return cachedBody;
    }

    public String getContentAsString() {
        return new String(cachedBody, StandardCharsets.UTF_8);
    }

    @Override
    public String getParameter(String name) {
        if (!CollectionUtils.isEmpty(extParams) && extParams.containsKey(name)) {
            String[] value = extParams.get(name);
            return value == null || value.length == 0 ? null : value[0];
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (CollectionUtils.isEmpty(extParams)) {
            return super.getParameterMap();
        }
        HashMap<String, String[]> map = new HashMap<>(super.getParameterMap());
        map.putAll(extParams);
        return map;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (CollectionUtils.isEmpty(extParams)) {
            return super.getParameterNames();
        }
        Set<String> names = new HashSet<>(extParams.keySet());
        Enumeration<String> parameterNames = super.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            names.add(parameterNames.nextElement());
        }
        return new IteratorEnumeration<>(names.iterator());
    }

    @Override
    public String[] getParameterValues(String name) {
        if (!CollectionUtils.isEmpty(extParams) && extParams.containsKey(name)) {
            return extParams.get(name);
        }
        return super.getParameterValues(name);
    }

    public void addParameter(String name, String value) {
        addParameter(name, new String[]{value});
    }

    public void addParameter(String name, String[] value) {
        if (extParams == null) {
            extParams = new HashMap<>();
        }
        extParams.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (null == value && extHeaders != null) {
            value = extHeaders.get(name);
        }
        return value;
    }

    public void addHeader(String name, String value) {
        if (extHeaders == null) {
            extHeaders = new HashMap<>();
        }
        extHeaders.put(name, value);
    }

    public boolean isEmptyBody() {
        return cachedBody == null || cachedBody.length == 0;
    }


    private static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream is;

        private CachedServletInputStream(byte[] body) {
            this.is = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return is.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return is.read();
        }
    }
}
