package github.buriedincode.gallagher.services;

import static github.buriedincode.gallagher.utilities.JsonUtilities.getNestedValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.buriedincode.gallagher.exceptions.NotFoundException;
import github.buriedincode.gallagher.exceptions.ValidationException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class GallagherServiceImpl implements GallagherService {
  private final OkHttpClient httpClient;
  private final ObjectMapper objectMapper;

  @Value("${gallagher.base-url}")
  private String baseUrl;

  @Override
  public Map<String, Object> createUser(Map<String, Object> data)
      throws IOException, ValidationException, NotFoundException {
    var details = fetchDetails();
    String cardholderUrl = getNestedValue(details, "features", "cardholders", "cardholders", "href");
    if (cardholderUrl == null) {
      return Collections.emptyMap();
    }
    String requestBody;
    try {
      requestBody = objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException jpe) {
      throw new ValidationException("Unable to parse request body: %s".formatted(jpe.getMessage()), jpe);
    }
    var request = new Request.Builder().url(cardholderUrl)
        .post(RequestBody.create(requestBody, MediaType.get("application/json"))).build();
    try {
      return makeRequest(request);
    } catch (NotFoundException nfe) {
      throw new NotFoundException("Unable to create cardholder", nfe);
    }
  }

  @Override
  public Map<String, Object> deleteUser() throws IOException, ValidationException, NotFoundException {
    return Map.of();
  }

  @SuppressWarnings("unchecked")
  @NotNull
  public Map<String, Object> searchUser(String email) throws IOException, ValidationException, NotFoundException {
    var details = fetchDetails();
    String cardholderUrl = getNestedValue(details, "features", "cardholders", "cardholders", "href");
    if (cardholderUrl == null) {
      throw new ValidationException("Unable to parse cardholders url");
    }

    var request = new Request.Builder().url(cardholderUrl).get().build();
    try {
      List<Map<String, Object>> response = (List<Map<String, Object>>) makeRequest(request)
          .getOrDefault("results", Collections.emptyList());
      if (response.isEmpty())
        throw new NotFoundException();
      return response.getFirst();
    } catch (NotFoundException nfe) {
      throw new NotFoundException("Unable to find cardholder", nfe);
    }
  }

  @Override
  public Map<String, Object> updateUser() throws IOException, ValidationException, NotFoundException {
    return Map.of();
  }

  @NotNull
  private Map<String, Object> fetchDetails() throws IOException, ValidationException, NotFoundException {
    var apiUrl = "%s/api".formatted(baseUrl);
    var request = new Request.Builder().url(apiUrl).get().build();
    try {
      return makeRequest(request);
    } catch (NotFoundException nfe) {
      throw new NotFoundException("Endpoint not found", nfe);
    }
  }

  private Map<String, Object> makeRequest(Request request)
      throws IOException, ValidationException, NotFoundException {
    try (var response = httpClient.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        if (response.code() == 404)
          throw new NotFoundException();
        log.error("Failed to call {}: {}", response.request().url(), response);
        throw new IOException("Unexpected response code: " + response.code());
      }
      log.info("Received response from {}", response.request().url());

      var typeRef = new TypeReference<Map<String, Object>>() {
      };
      if (response.body() != null) {
        return objectMapper.readValue(response.body().string(), typeRef);
      }
      return Collections.emptyMap();
    } catch (JsonProcessingException jpe) {
      throw new ValidationException("Unable to parse response: %s".formatted(jpe.getMessage()), jpe);
    } catch (IOException ioe) {
      log.info("Error calling {}: {}", request.url(), ioe.getMessage());
      throw ioe;
    }
  }
}
