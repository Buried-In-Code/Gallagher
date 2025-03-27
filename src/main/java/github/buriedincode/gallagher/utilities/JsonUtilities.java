package github.buriedincode.gallagher.utilities;

import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtilities {
  @SuppressWarnings("unchecked")
  @Nullable
  public static <T> T getNestedValue(Map<String, Object> data, String... keys) {
    var currentMap = data;
    for (var i = 0; i < keys.length - 1; i++) {
      var value = currentMap.getOrDefault(keys[i], Collections.emptyMap());
      if (!(value instanceof Map)) {
        return null;
      }
      currentMap = (Map<String, Object>) value;
    }
    return (T) currentMap.get(keys[keys.length - 1]);
  }
}
