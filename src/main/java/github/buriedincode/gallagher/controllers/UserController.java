package github.buriedincode.gallagher.controllers;

import github.buriedincode.gallagher.configurations.UserProperties;
import github.buriedincode.gallagher.models.UserResponse;
import github.buriedincode.gallagher.services.GallagherService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@Slf4j
@RequiredArgsConstructor
public class UserController {
  private final GallagherService gallagherService;
  private final UserProperties userProperties;

  @PostMapping("/create")
  public ResponseEntity<Map<String, Object>> createUser() {
    log.info("Request to create user");

    gallagherService.createCardholder(userProperties.getUser());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser() {
    log.info("Request to delete user");

    var cardholder = gallagherService.searchCardholder(userProperties.getEmail());
    var cardholderId = cardholder == null ? null : cardholder.id();
    if (cardholderId == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    gallagherService.deleteCardholder(cardholderId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/read")
  public ResponseEntity<UserResponse> readUser() {
    log.info("Request to read user");
    var cardholderSummary = gallagherService.searchCardholder(userProperties.getEmail());
    var cardholder = cardholderSummary == null ? null : gallagherService.getCardholder(cardholderSummary.id());
    if (cardholder == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(UserResponse.fromCardholder(cardholder), HttpStatus.OK);
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateUser() {
    log.info("Request to update user");

    var cardholder = gallagherService.searchCardholder(userProperties.getEmail());
    var cardholderId = cardholder == null ? null : cardholder.id();
    if (cardholderId == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    gallagherService.updateCardholder(cardholderId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
