package github.buriedincode.gallagher.services;

import github.buriedincode.gallagher.models.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GallagherService {
  void addCardToCardholder(long cardholderId, @NotNull AddCardRequest newCard);

  void createCardholder(@NotNull UserRequest newUser);

  void deleteCardholder(long cardholderId);

  @NotNull
  Cardholder getCardholder(long cardholderId);

  @Nullable
  CardholderSummary searchCardholder(@NotNull String email);

  @Nullable
  PersonalDataFieldSummary searchPDF(@NotNull String name);
}
