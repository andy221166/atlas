package org.atlas.platform.json;

import java.util.List;

public interface Json {

  <T> T toObject(String source, Class<T> type);

  <T> List<T> toList(String source, Class<T> type);

  String toJson(Object source);

  Object getNodeValue(String json, String key);
}
