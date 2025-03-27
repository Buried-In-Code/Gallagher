package github.buriedincode.gallagher.services;

import github.buriedincode.gallagher.exceptions.NotFoundException;
import github.buriedincode.gallagher.exceptions.ValidationException;
import java.io.IOException;
import java.util.Map;

public interface GallagherService {
  Map<String, Object> createUser(Map<String, Object> data) throws IOException, ValidationException, NotFoundException;

  Map<String, Object> deleteUser() throws IOException, ValidationException, NotFoundException;

  Map<String, Object> searchUser(String email) throws IOException, ValidationException, NotFoundException;

  Map<String, Object> updateUser() throws IOException, ValidationException, NotFoundException;
}
