package github.buriedincode.gallagher.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record UserRequest(String firstName, String lastName, Link division,
    @JsonProperty("@[Trial] Email Address") String email, @JsonProperty("@[Trial] External ID") Object externalId,
    List<AccessGroup> accessGroups) {
  public record Link(String href) {
  }

  public record AccessGroup(@JsonProperty("accessgroup") Link accessGroup) {
  }
}
