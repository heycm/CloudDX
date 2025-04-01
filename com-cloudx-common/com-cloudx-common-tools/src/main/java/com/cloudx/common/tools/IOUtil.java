package com.cloudx.common.tools;


import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * IO操作
 * @author heycm
 * @since 2024-11-16 13:36
 */
public class IOUtil {

    private IOUtil() {
    }

    /**
     * 设置输出二进制文件响应头
     * @param response 响应
     * @param fileName 文件名
     */
    private static void setOctetStreamResponse(HttpServletResponse response, String fileName) {
        response.setContentType("application/octet-stream");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20").replaceAll("/", "_");
        response.addHeader("Content-Disposition", String.format("attachment; filename*=UTF-8''%s", fileName));
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
    }

    /**
     * 响应JSON串
     * @param response HttpServletResponse
     * @param output   返回对象
     */
    public static void writeJson(HttpServletResponse response, Object output) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json");
        response.getWriter().println(Jackson.toJson(output));
        response.getWriter().flush();
    }

    /**
     * 输出文件
     * @param response 响应
     * @param file     文件
     * @throws IOException IOException
     */
    public static void writeFile(HttpServletResponse response, File file) throws IOException {
        setOctetStreamResponse(response, file.getName());
        try (InputStream is = Files.newInputStream(file.toPath()); ServletOutputStream os = response.getOutputStream()) {
            IOUtil.isToOs(is, os);
        }
    }

    /**
     * 输入流转输出流
     * @param is 输入流
     * @param os 输出流
     * @throws IOException IOException
     */
    public static void isToOs(InputStream is, OutputStream os) throws IOException {
        try (BufferedInputStream bis = new BufferedInputStream(is); BufferedOutputStream bos = new BufferedOutputStream(os)) {
            IOUtil.isToOs(bis, bos);
        }
    }

    /**
     * 输入流转输出流
     * @param buffIs 输入流
     * @param buffOs 输出流
     * @throws IOException IOException
     */
    public static void isToOs(BufferedInputStream buffIs, BufferedOutputStream buffOs) throws IOException {
        byte[] buff = new byte[1024];
        int len;
        while ((len = buffIs.read(buff)) != -1) {
            buffOs.write(buff, 0, len);
        }
        buffOs.flush();
    }
}
