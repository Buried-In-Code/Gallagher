package github.buriedincode.gallagher.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "read.user")
@Getter
@Setter
public class ReadProperties {
  private String email;
}
