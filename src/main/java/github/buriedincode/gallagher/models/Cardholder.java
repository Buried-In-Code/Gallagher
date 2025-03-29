package github.buriedincode.gallagher.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Cardholder(List<AccessGroup> accessGroups, boolean authorised, List<Object> cards, Division division,
    String firstName, String href, long id, String lastName, Map<String, Object> additionalFields) {
  public Cardholder {
    accessGroups = accessGroups == null ? new ArrayList<>() : accessGroups;
    cards = cards == null ? new ArrayList<>() : cards;
    additionalFields = additionalFields == null ? new HashMap<>() : additionalFields;
  }

  @JsonAnySetter
  public void addAdditionalFields(String key, Object value) {
    if (key.startsWith("@")) {
      this.additionalFields.put(key, value);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record AccessGroup(Link accessGroup) {
  }

  public record Division(String href, long id) {
  }

  public record Link(String href, String name) {
  }
}
