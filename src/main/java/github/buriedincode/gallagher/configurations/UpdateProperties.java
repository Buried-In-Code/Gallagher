package github.buriedincode.gallagher.configurations;

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
  private String cardNumber;

  @Value("${update.user.email}")
  private String userEmail;
}
