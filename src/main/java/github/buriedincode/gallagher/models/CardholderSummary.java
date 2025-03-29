package github.buriedincode.gallagher.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import java.util.HashMap;
import java.util.Map;

public record CardholderSummary(String href, long id, Map<String, Object> additionalFields) {
  public CardholderSummary {
    additionalFields = additionalFields == null ? new HashMap<>() : additionalFields;
  }

  @JsonAnySetter
  public void addAdditionalFields(String key, Object value) {
    if (key.startsWith("@")) {
      this.additionalFields.put(key, value);
    }
  }
}
