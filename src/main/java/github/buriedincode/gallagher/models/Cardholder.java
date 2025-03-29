package github.buriedincode.gallagher.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Cardholder(String firstName, String lastName, @JsonProperty("@email") String email, long division) {
}
