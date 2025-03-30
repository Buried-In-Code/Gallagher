package github.buriedincode.gallagher.models;

import org.jetbrains.annotations.NotNull;

public record NamedLink(@NotNull String href, @NotNull String name) {
}
