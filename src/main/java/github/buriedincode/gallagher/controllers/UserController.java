package github.buriedincode.gallagher.controllers;

import github.buriedincode.gallagher.configurations.CreateProperties;
import github.buriedincode.gallagher.configurations.DeleteProperties;
import github.buriedincode.gallagher.configurations.ReadProperties;
import github.buriedincode.gallagher.configurations.UpdateProperties;
import github.buriedincode.gallagher.exceptions.ConflictException;
import github.buriedincode.gallagher.exceptions.NotFoundException;
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
  private final CreateProperties createProperties;
  private final ReadProperties readProperties;
  private final UpdateProperties updateProperties;
  private final DeleteProperties deleteProperties;

  @PostMapping("/create")
  public ResponseEntity<Map<String, Object>> createUser() {
    log.info("Request to create user");

    var cardholder = gallagherService.searchCardholder(createProperties.getEmail());
    if (cardholder != null)
      throw new ConflictException("Cardholder already exists");

    gallagherService.createCardholder(createProperties.getUser());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser() {
    log.info("Request to delete user");

    var cardholder = gallagherService.searchCardholder(deleteProperties.getEmail());
    var cardholderId = cardholder == null ? null : cardholder.id();
    if (cardholderId == null) {
      throw new NotFoundException("Cardholder not found");
    }
    gallagherService.deleteCardholder(cardholderId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/read")
  public ResponseEntity<UserResponse> readUser() {
    log.info("Request to read user");
    var cardholderSummary = gallagherService.searchCardholder(readProperties.getEmail());
    var cardholder = cardholderSummary == null ? null : gallagherService.getCardholder(cardholderSummary.id());
    if (cardholder == null) {
      throw new NotFoundException("Cardholder not found");
    }
    return new ResponseEntity<>(UserResponse.fromCardholder(cardholder), HttpStatus.OK);
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateUser() {
    log.info("Request to update user");

    var cardholder = gallagherService.searchCardholder(updateProperties.getUserEmail());
    var cardholderId = cardholder == null ? null : cardholder.id();
    if (cardholderId == null) {
      throw new NotFoundException("Cardholder not found");
    }
    gallagherService.updateCardholder(cardholderId);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
