package org.atlas.infrastructure.api.server.rest.core.logging;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.atlas.framework.util.StringUtil;


@Slf4j
public class RequestLoggingUtil {

  public static void logRequest(CachedHttpServletRequest request, int maxPayloadLength) {
    String ipAddress = IpAddressUtil.getIpAddress(request);
    String method = request.getMethod();
    String fullUrl = getFullUrl(request);
    Map<String, String> headers = getHeaders(request);
    String body = getBody(request, maxPayloadLength);
    String parameters = getParameters(request);
    log.info(
        "Incoming request:\nIP address: {}\nMethod = {}\nURL = {}\nHeaders = {}\nBody = {}\nParameters = {}",
        ipAddress, method, fullUrl, headers, body, parameters);
  }

  private static String getFullUrl(HttpServletRequest request) {
    StringBuffer requestUrl = request.getRequestURL();
    String queryString = request.getQueryString();
    if (queryString != null) {
      requestUrl.append("?").append(queryString);
    }
    return requestUrl.toString();
  }

  private static Map<String, String> getHeaders(HttpServletRequest request) {
    Map<String, String> headers = new HashMap<>();
    Collections.list(request.getHeaderNames())
        .forEach(headerName -> headers.put(headerName, request.getHeader(headerName)));
    return headers;
  }

  private static String getBody(HttpServletRequest request, int maxPayloadLength) {
    try {
      String body = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
      return StringUtil.limit(body, maxPayloadLength);
    } catch (IOException e) {
      log.error("Failed to cache request body", e);
      return StringUtil.EMPTY;
    }
  }

  private static String getParameters(HttpServletRequest request) {
    Map<String, String[]> parameterMap = request.getParameterMap();
    if (parameterMap == null) {
      return "";
    }
    StringJoiner joiner = new StringJoiner("&");
    for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
      String key = entry.getKey();
      String[] values = entry.getValue();
      String combinedValues = Stream.of(values)
          .map(RequestLoggingUtil::urlEncode)
          .collect(Collectors.joining(","));
      joiner.add(urlEncode(key) + "=" + combinedValues);
    }
    return joiner.toString();
  }

  // Helper method to handle URL encoding. E.g. `http://example.com/profile?name=John Doe & Sons` needs encoding to
  // `http://example.com/profile?name=John%20Doe%20%26%20Sons`
  private static String urlEncode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }
}
