package github.buriedincode.gallagher.configurations;

import github.buriedincode.gallagher.models.UserRequest;
import github.buriedincode.gallagher.models.UserRequest.AccessGroup;
import github.buriedincode.gallagher.models.UserRequest.Link;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "create.user")
@Getter
@Setter
public class CreateProperties {
  private String email;
  private String firstName;
  private String lastName;
  private String divisionHref;
  private String accessGroupHref;
  private String externalId;

  public UserRequest getUser() {
    return new UserRequest(firstName, lastName, new Link(divisionHref), email, externalId,
        List.of(new AccessGroup(new Link(accessGroupHref))));
  }
}
