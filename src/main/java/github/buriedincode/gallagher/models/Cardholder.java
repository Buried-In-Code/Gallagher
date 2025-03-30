package github.buriedincode.gallagher.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public record Cardholder(List<AccessGroup> accessGroups, boolean authorised, List<Object> cards, Division division,
    @NotNull String firstName, @NotNull String href, long id, @NotNull String lastName,
    Map<String, Object> additionalFields) {
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
  public record AccessGroup(@NotNull NamedLink accessGroup) {
  }

  public record Division(@NotNull String href, long id) {
  }
}
