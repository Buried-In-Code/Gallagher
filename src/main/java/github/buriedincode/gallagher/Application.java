package github.buriedincode.gallagher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  public static void main(String... args) {
    System.out.printf("Java v%s%n", System.getProperty("java.version"));
    System.out.printf("Arch: %s%n", System.getProperty("os.arch"));
    SpringApplication.run(Application.class, args);
  }
}
