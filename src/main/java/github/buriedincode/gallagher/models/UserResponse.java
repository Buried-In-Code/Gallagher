package github.buriedincode.gallagher.models;

import java.util.Map;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

public record UserResponse(String firstName, String lastName, Map<String, String> personalDataDefinitions,
    int accessGroupCount, int cardCount) {
  public static UserResponse fromCardholder(@NotNull Cardholder cardholder) {
    return new UserResponse(cardholder.firstName(), cardholder.lastName(),
        cardholder.additionalFields().entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> String.valueOf(entry.getValue()))),
        cardholder.accessGroups().size(), cardholder.cards().size());
  }
}
