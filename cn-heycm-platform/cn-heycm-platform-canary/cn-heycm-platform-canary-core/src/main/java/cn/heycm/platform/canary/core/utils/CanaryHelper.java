package cn.heycm.platform.canary.core.utils;

import java.util.Optional;
import java.util.Random;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

/**
 * @author hey
 * @version 1.0
 * @since 2025/3/5 14:23
 */
public class CanaryHelper {

    private static final Random RANDOM = new Random();

    /**
     * 检查指定IP地址是否属于给定的CIDR范围
     * @param cidr CIDR格式的字符串，表示IP地址范围（如："192.168.1.0/24"）
     * @param ip   要验证的IPv4地址字符串（如："192.168.1.5"）
     * @return true表示IP在CIDR范围内，false表示不在范围内或参数格式非法
     */
    public static boolean isIpRange(String cidr, String ip) {
        if (cidr == null || ip == null) {
            return false;
        }

        String[] cidrParts = cidr.split("/");
        if (cidrParts.length != 2) {
            return false;
        }

        String cidrIpStr = cidrParts[0];
        String maskStr = cidrParts[1];

        int cidrIp = ipToInt(cidrIpStr);
        if (cidrIp == -1) {
            return false;
        }

        int maskBitCount;
        try {
            maskBitCount = Integer.parseInt(maskStr);
        } catch (NumberFormatException e) {
            return false;
        }

        if (maskBitCount < 0 || maskBitCount > 32) {
            return false;
        }

        // 32位网络掩码
        int maskValue = (maskBitCount == 0) ? 0 : (-1 << (32 - maskBitCount));

        int targetIp = ipToInt(ip);
        if (targetIp == -1) {
            return false;
        }

        // 网段IP和目标IP分别跟网络掩码按位与，得到各自的网络地址，若相当则表示在同一网段
        int cidrNetwork = cidrIp & maskValue;
        int targetNetwork = targetIp & maskValue;

        return cidrNetwork == targetNetwork;
    }

    private static int ipToInt(String ip) {
        if (ip == null) {
            return -1;
        }

        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return -1;
        }

        try {
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            int c = Integer.parseInt(parts[2]);
            int d = Integer.parseInt(parts[3]);

            if (a < 0 || a > 255 || b < 0 || b > 255 || c < 0 || c > 255 || d < 0 || d > 255) {
                return -1;
            }

            return (a << 24) | (b << 16) | (c << 8) | d;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * 获取客户端IP地址
     * @param headers 请求头信息
     * @return IP
     */
    public static String getRemoteIpAddress(HttpHeaders headers) {
        String ipAddress = Optional.ofNullable(headers.getFirst("X-Forwarded-For")).filter(list -> !list.isEmpty()).orElse(null);
        if (!StringUtils.hasText(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = Optional.ofNullable(headers.getFirst("X-Real-IP")).filter(list -> !list.isEmpty()).orElse(null);
        }
        if (!StringUtils.hasText(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = Optional.ofNullable(headers.getFirst("Proxy-Client-IP")).filter(list -> !list.isEmpty()).orElse(null);
        }
        if (!StringUtils.hasText(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = Optional.ofNullable(headers.getFirst("WL-Proxy-Client-IP")).filter(list -> !list.isEmpty()).orElse(null);
        }
        if (!StringUtils.hasText(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = Optional.ofNullable(headers.getFirst("HTTP_CLIENT_IP")).filter(list -> !list.isEmpty()).orElse(null);
        }
        if (!StringUtils.hasText(ipAddress) || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = Optional.ofNullable(headers.getFirst("HTTP_X_FORWARDED_FOR")).filter(list -> !list.isEmpty()).orElse(null);
        }
        if (StringUtils.hasText(ipAddress) && ipAddress.contains(",")) {
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress != null ? ipAddress.trim() : "unknown";
    }

    /**
     * 按比例流量标记
     * @param proportion 比例
     * @return true表示标记流量，false表示不标记流量
     */
    public static boolean markFlow(int proportion) {
        if (proportion > 100 || proportion < 0) {
            return false;
        }
        int r = RANDOM.nextInt(100) + 1;
        return r <= proportion;
    }
}
