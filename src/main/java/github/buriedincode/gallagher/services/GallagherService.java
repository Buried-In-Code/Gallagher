package github.buriedincode.gallagher.services;

import java.util.Map;

public interface GallagherService {
  void createUser(Map<String, Object> data);

  void deleteUser(long cardholderId);

  Map<String, Object> searchUser(String email);

  Map<String, Object> updateUser();
}
