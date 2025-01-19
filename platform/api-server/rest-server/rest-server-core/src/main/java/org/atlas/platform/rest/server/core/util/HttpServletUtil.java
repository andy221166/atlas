package org.atlas.platform.rest.server.core.util;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.atlas.commons.util.json.JsonUtil;
import org.springframework.http.HttpStatus;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpServletUtil {

  public static void respondJson(HttpServletResponse response, Object payload,
      HttpStatus httpStatus) throws IOException {
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setStatus(httpStatus.value());

    PrintWriter out = response.getWriter();
    out.print(JsonUtil.toJson(payload));
    out.flush();
  }
}
