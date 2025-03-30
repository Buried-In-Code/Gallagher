package github.buriedincode.gallagher.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "delete.user")
@Getter
@Setter
public class DeleteProperties {
  private String email;
}
