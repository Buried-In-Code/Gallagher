package github.buriedincode.gallagher.controllers;

import github.buriedincode.gallagher.exceptions.NotFoundException;
import github.buriedincode.gallagher.exceptions.ValidationException;
import github.buriedincode.gallagher.services.GallagherService;
import java.io.IOException;
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

  @PostMapping("/create")
  public ResponseEntity<Map<String, Object>> createUser() {
    log.info("Request to create user");
    try {
      var details = gallagherService
          .createUser(Map.of("firstName", "John", "lastName", "Smith", "@Email", "john@onugo.com"));
      log.info("Details: {}", details);
      return new ResponseEntity<>(details, HttpStatus.OK);
    } catch (ValidationException ve) {
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (NotFoundException nfe) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IOException ioe) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser() {
    log.info("Request to delete user");
    try {
      var user = readUser().getBody();
      var userId = user == null ? null : user.get("id");
      if (userId == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      gallagherService.deleteUser((Long) userId);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (ValidationException ve) {
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (NotFoundException nfe) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IOException ioe) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/read")
  public ResponseEntity<Map<String, Object>> readUser() {
    log.info("Request to get user");
    try {
      var details = gallagherService.searchUser("john@onugo.com");
      log.info("Details: {}", details);
      return new ResponseEntity<>(details, HttpStatus.OK);
    } catch (ValidationException ve) {
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    } catch (NotFoundException nfe) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (IOException ioe) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateUser() {
    log.info("Request to update user");
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
