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
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    if (cardholder != null) {
      throw new ConflictException("Cardholder already exists");
    }

    gallagherService.createCardholder(createProperties.getUser());
    return ResponseEntity.ok().build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser() {
    log.info("Request to delete user");

    var cardholder = Optional.ofNullable(gallagherService.searchCardholder(deleteProperties.getEmail()))
        .orElseThrow(() -> new NotFoundException("Cardholder not found"));
    gallagherService.deleteCardholder(cardholder.id());

    return ResponseEntity.ok().build();
  }

  @GetMapping("/read")
  public ResponseEntity<UserResponse> readUser() {
    log.info("Request to read user");

    var cardholder = Optional.ofNullable(gallagherService.searchCardholder(readProperties.getEmail()))
        .map(summary -> gallagherService.getCardholder(summary.id()))
        .orElseThrow(() -> new NotFoundException("Cardholder not found"));

    return ResponseEntity.ok(UserResponse.fromCardholder(cardholder));
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateUser() {
    log.info("Request to update user");

    var cardholder = Optional.ofNullable(gallagherService.searchCardholder(updateProperties.getUserEmail()))
        .orElseThrow(() -> new NotFoundException("Cardholder not found"));
    var cardExists = cardholder.cards().stream()
        .anyMatch(card -> card.type().href().equals(updateProperties.getCardTypeHref())
            && card.number() == updateProperties.getCardNumber());
    if (cardExists) {
      throw new ConflictException("Cardholder already has this card");
    }
    gallagherService.addCardToCardholder(cardholder.id(), updateProperties.getCard());

    return ResponseEntity.ok().build();
  }
}
