package github.buriedincode.gallagher.models;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record CardholderSummary(@NotNull String href, long id, List<Card> cards, Map<String, Object> additionalFields) {
  public CardholderSummary {
    cards = cards == null ? new ArrayList<>() : cards;
    additionalFields = additionalFields == null ? new HashMap<>() : additionalFields;
  }

  @JsonAnySetter
  public void addAdditionalFields(@NotNull String key, @Nullable Object value) {
    if (key.startsWith("@")) {
      this.additionalFields.put(key, value);
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  public record Card(@NotNull String href, long number, @NotNull NamedLink type) {
  }
}
