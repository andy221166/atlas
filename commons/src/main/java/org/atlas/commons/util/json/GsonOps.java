package org.atlas.commons.util.json;

import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonOps implements JsonOps {

  private static final com.google.gson.Gson gson;

  static {
    GsonBuilder gsonBuilder = new GsonBuilder();
    gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
    gsonBuilder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    gson = gsonBuilder.create();
  }

  @Override
  public <T> T toObject(String source, Class<T> objectClass) {
    return gson.fromJson(source, objectClass);
  }

  @Override
  public <T> List<T> toList(String source, Class<T> type) {
    Type listType = new TypeToken<ArrayList<T>>() {
    }.getType();
    return gson.fromJson(source, listType);
  }

  @Override
  public String toJson(Object source) {
    return gson.toJson(source);
  }
}
