package github.buriedincode.gallagher.services;

import github.buriedincode.gallagher.models.Cardholder;
import github.buriedincode.gallagher.models.CardholderSummary;
import github.buriedincode.gallagher.models.PersonalDataFieldSummary;
import github.buriedincode.gallagher.models.UserRequest;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GallagherService {
  void createCardholder(@NotNull UserRequest newUser);

  void deleteCardholder(long cardholderId);

  @NotNull
  Cardholder getCardholder(long cardholderId);

  @Nullable
  CardholderSummary searchCardholder(@NotNull String email);

  @Nullable
  PersonalDataFieldSummary searchPDF(@NotNull String name);

  void updateCardholder(long cardholderId);
}
