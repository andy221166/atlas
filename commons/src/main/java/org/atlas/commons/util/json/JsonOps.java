package org.atlas.commons.util.json;

import java.util.List;

public interface JsonOps {

  <T> T toObject(String source, Class<T> type);

  <T> List<T> toList(String source, Class<T> type);

  String toJson(Object source);
}
