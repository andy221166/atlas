package org.atlas.platform.json.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.text.SimpleDateFormat;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.atlas.commons.constant.Constant;
import org.atlas.commons.util.StringUtil;
import org.atlas.platform.json.Json;

@Slf4j
public class JacksonAdapter implements Json {

  public static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.setDateFormat(new SimpleDateFormat(Constant.DATE_TIME_FORMAT));
  }

  @Override
  public <T> T toObject(String source, Class<T> type) {
    try {
      return objectMapper.readValue(source, type);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public <T> List<T> toList(String source, Class<T> type) {
    try {
      return objectMapper.readValue(source,
          objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toJson(Object source) {
    try {
      return objectMapper.writer().writeValueAsString(source);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getNodeValue(String json, String key) {
    try {
      JsonNode tree = objectMapper.readTree(json);
      JsonNode valueNode = tree.get(key);
      if (valueNode != null) {
        return valueNode.asText();
      } else {
        log.warn("Key '{}' not found in the JSON", key);
        return StringUtil.EMPTY;
      }
    } catch (JsonProcessingException e) {
      log.error("Failed to parse JSON", e);
      return StringUtil.EMPTY;
    }
  }
}
