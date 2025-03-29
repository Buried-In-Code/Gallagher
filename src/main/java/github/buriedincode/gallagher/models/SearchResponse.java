package github.buriedincode.gallagher.models;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record SearchResponse<T>(@NotNull List<T> results, @Nullable String searchWarningMessage) {
}
