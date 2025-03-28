package github.buriedincode.gallagher.controllers;

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

  @PostMapping("/create")
  public ResponseEntity<Map<String, Object>> createUser() {
    log.info("Request to create user");

    gallagherService.createUser(Map.of("firstName", "John", "lastName", "Smith", "@email", "john@onugo.com"));
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteUser() {
    log.info("Request to delete user");

    var user = readUser().getBody();
    var userId = user == null ? null : user.get("id");
    if (userId == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    gallagherService.deleteUser((Long) userId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/read")
  public ResponseEntity<Map<String, Object>> readUser() {
    var email = "john@onugo.com";

    log.info("Request to read user: {}", email);
    var details = gallagherService.searchUser(email);
    log.info("Details: {}", details);
    return new ResponseEntity<>(details, HttpStatus.OK);
  }

  @PutMapping("/update")
  public ResponseEntity<Void> updateUser() {
    log.info("Request to update user");
    return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
  }
}
