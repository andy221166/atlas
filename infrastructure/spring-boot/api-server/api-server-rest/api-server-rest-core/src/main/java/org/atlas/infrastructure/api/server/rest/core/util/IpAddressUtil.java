package org.atlas.infrastructure.api.server.rest.core.util;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

public class IpAddressUtil {

  private static final String[] IP_HEADER_CANDIDATES = {
      "x-forwarded-for",
      "Proxy-Client-IP",
      "WL-Proxy-Client-IP",
      "X-Real-IP"
  };

  private IpAddressUtil() {
  }

  public static String getIpAddress(HttpServletRequest request) {
    for (String header : IP_HEADER_CANDIDATES) {
      String ipAddress = request.getHeader(header);
      if (isValidIpAddress(ipAddress)) {
        return extractFirstIpAddress(ipAddress);
      }
    }
    return request.getRemoteAddr();
  }

  private static boolean isValidIpAddress(String ipAddress) {
    return StringUtils.hasText(ipAddress) && !"unknown".equalsIgnoreCase(ipAddress);
  }

  private static String extractFirstIpAddress(String ipAddress) {
    if (ipAddress != null && ipAddress.length() > 15 && ipAddress.contains(",")) {
      return ipAddress.substring(0, ipAddress.indexOf(","));
    }
    return ipAddress;
  }
}
