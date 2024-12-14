package org.atlas.commons.util.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.util.base.StringUtil;

@Slf4j
public class FastjsonOps implements JsonOps {

  @Override
  public <T> T toObject(String source, Class<T> objectClass) {
    return JSON.parseObject(source, objectClass);
  }

  @Override
  public <T> List<T> toList(String source, Class<T> type) {
    return JSON.parseArray(source, type);
  }

  @Override
  public String toJson(Object source) {
    return JSON.toJSONString(source);
  }

  @Override
  public String getNodeValue(String json, String key) {
    try {
      JSONObject jsonObject = JSON.parseObject(json);
      return Optional.ofNullable(jsonObject.getString(key))
          .orElse(StringUtil.EMPTY);
    } catch (Exception e) {
      log.error("Failed to parse JSON", e);
      return StringUtil.EMPTY;
    }
  }
}
