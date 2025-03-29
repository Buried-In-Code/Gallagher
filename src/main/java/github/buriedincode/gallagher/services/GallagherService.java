package github.buriedincode.gallagher.services;

import github.buriedincode.gallagher.models.Cardholder;
import java.util.Map;

public interface GallagherService {
  void createCardholder(Cardholder newCardholder);

  void deleteCardholder(long cardholderId);

  Map<String, Object> searchCardholder(String email);

  Map<String, Object> updateCardholder(long cardholderId);
}
