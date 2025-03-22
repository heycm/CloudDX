package cn.heycm.common.web.filter.wrapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import org.springframework.util.FastByteArrayOutputStream;

/**
 * 可重复读响应装饰器
 * @author hey
 * @version 1.0
 * @since 2024/12/5 18:06
 */
public class CachedResponseWrapper extends HttpServletResponseWrapper {

    private FastByteArrayOutputStream cachedBody = new FastByteArrayOutputStream();
    private PrintWriter cachedWriter;
    private ServletOutputStream cachedOutputStream;
    private boolean responseWritten = false;

    public CachedResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (null == cachedOutputStream) {
            cachedOutputStream = new CachedServletOutputStream(cachedBody);
        }
        return cachedOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (null == cachedWriter) {
            cachedWriter = new PrintWriter(cachedBody, true);
        }
        return cachedWriter;
    }

    public byte[] getContentAsByteArray() {
        return cachedBody.toByteArray();
    }

    public String getContentAsString() {
        return new String(cachedBody.toByteArray(), StandardCharsets.UTF_8);
    }

    public void setBody(byte[] body) throws IOException {
        if (responseWritten) {
            return;
        }
        cachedBody.reset();
        cachedBody.write(body);
    }

    public void writeToClient() throws IOException {
        if (responseWritten) {
            return;
        }

        byte[] content = getContentAsByteArray();

        // 设置 Content-Length 以确保客户端知道响应体的长度
        if (!containsHeader("Content-Length")) {
            setContentLength(content.length);
        }

        ServletOutputStream outputStream = super.getOutputStream();
        outputStream.write(content);
        outputStream.flush();

        responseWritten = true;
    }

    private static class CachedServletOutputStream extends ServletOutputStream {

        private final FastByteArrayOutputStream os;

        private CachedServletOutputStream(FastByteArrayOutputStream os) {
            this.os = os;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void write(int b) throws IOException {
            os.write(b);
        }
    }
}
