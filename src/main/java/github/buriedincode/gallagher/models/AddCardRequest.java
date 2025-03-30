package github.buriedincode.gallagher.models;

import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public final class AddCardRequest {
  @NotNull
  private final Map<String, Card[]> cards;

  public AddCardRequest(Card... cards) {
    this.cards = Map.of("add", cards);
  }

  public record Card(Link type, long number) {
  }
}
