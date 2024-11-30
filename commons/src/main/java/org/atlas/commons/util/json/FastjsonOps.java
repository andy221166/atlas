package org.atlas.commons.util.json;

import com.alibaba.fastjson2.JSON;
import java.util.List;

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
}
