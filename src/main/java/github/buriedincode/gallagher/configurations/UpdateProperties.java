package github.buriedincode.gallagher.configurations;

import github.buriedincode.gallagher.models.AddCardRequest;
import github.buriedincode.gallagher.models.AddCardRequest.Card;
import github.buriedincode.gallagher.models.Link;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "update")
@Getter
@Setter
public class UpdateProperties {
  @Value("${update.card.type-href}")
  private String cardTypeHref;

  @Value("${update.card.number}")
  private long cardNumber;

  @Value("${update.user.email}")
  private String userEmail;

  public AddCardRequest getCard() {
    return new AddCardRequest(new Card(new Link(cardTypeHref), cardNumber));
  }
}
