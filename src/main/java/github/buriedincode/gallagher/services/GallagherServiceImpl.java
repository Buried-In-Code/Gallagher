package github.buriedincode.gallagher.services;

import static github.buriedincode.gallagher.utilities.JsonUtilities.getNestedValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.buriedincode.gallagher.exceptions.BadRequestException;
import github.buriedincode.gallagher.exceptions.ConflictException;
import github.buriedincode.gallagher.exceptions.ForbiddenException;
import github.buriedincode.gallagher.exceptions.NotFoundException;
import github.buriedincode.gallagher.exceptions.UnexpectedException;
import github.buriedincode.gallagher.exceptions.ValidationException;
import github.buriedincode.gallagher.models.Cardholder;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
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
  public void createCardholder(Cardholder newCardholder) {
    var url = fetchEndpoint("features", "cardholders", "cardholders", "href");

    String requestBody;
    try {
      requestBody = objectMapper.writeValueAsString(newCardholder);
    } catch (JsonProcessingException jpe) {
      throw new ValidationException("Unable to parse request body: %s".formatted(jpe.getMessage()), jpe);
    }
    var request = new Request.Builder().url(url)
        .post(RequestBody.create(requestBody, MediaType.get("application/json"))).build();
    try (var response = httpClient.newCall(request).execute()) {
      if (response.code() == 201) {
        return;
      } else {
        var errorType = new TypeReference<Map<String, String>>() {
        };
        var errorMessage = objectMapper.readValue(response.body().string(), errorType).get("message");
        if (response.code() == 400) {
          throw new BadRequestException(errorMessage);
        } else if (response.code() == 403) {
          throw new ForbiddenException(errorMessage);
        } else {
          throw new UnexpectedException(
              "Unexpected response code: " + response.code() + " : " + errorMessage);
        }
      }
    } catch (IOException ioe) {
      log.info("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @Override
  public void deleteCardholder(long cardholderId) {
    var url = fetchEndpoint("features", "cardholders", "cardholders", "href");
    url = url.newBuilder().addPathSegment(String.valueOf(cardholderId)).build();

    var request = new Request.Builder().url(url).delete().build();
    try (var response = httpClient.newCall(request).execute()) {
      if (response.code() == 200 || response.code() == 204) {
        return;
      } else {
        var errorType = new TypeReference<Map<String, String>>() {
        };
        var errorMessage = objectMapper.readValue(response.body().string(), errorType).get("message");
        if (response.code() == 400) {
          throw new BadRequestException(errorMessage);
        } else if (response.code() == 403) {
          throw new ForbiddenException(errorMessage);
        } else if (response.code() == 404) {
          throw new NotFoundException(errorMessage);
        } else if (response.code() == 409) {
          throw new ConflictException(errorMessage);
        } else {
          throw new UnexpectedException(
              "Unexpected response code: " + response.code() + " : " + errorMessage);
        }
      }
    } catch (IOException ioe) {
      log.info("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @NotNull
  public Map<String, Object> searchCardholder(String email) {
    var url = fetchEndpoint("features", "cardholders", "cardholders", "href");
    url = url.newBuilder().addQueryParameter("@email", email)
        .addQueryParameter("fields", "defaults,personalDataFields").build();

    var request = new Request.Builder().url(url).get().build();
    try (var response = httpClient.newCall(request).execute()) {
      if (response.code() == 200) {
        if (response.body() != null) {
          try {
            var typeRef = new TypeReference<Map<String, List<Map<String, Object>>>>() {
            };
            var details = objectMapper.readValue(response.body().string(), typeRef).get("results");
            if (!details.isEmpty()) {
              return details.getFirst();
            }
          } catch (JsonProcessingException jpe) {
            throw new ValidationException("Unable to parse response: " + jpe.getMessage(), jpe);
          }
        }
        throw new NotFoundException("Unable to find cardholder.");
      } else {
        var errorType = new TypeReference<Map<String, String>>() {
        };
        var errorMessage = objectMapper.readValue(response.body().string(), errorType).get("message");
        if (response.code() == 400) {
          throw new BadRequestException(errorMessage);
        } else if (response.code() == 403) {
          throw new ForbiddenException(errorMessage);
        } else if (response.code() == 404) {
          throw new NotFoundException(errorMessage);
        } else {
          throw new UnexpectedException(
              "Unexpected response code: " + response.code() + " : " + errorMessage);
        }
      }
    } catch (IOException ioe) {
      log.info("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @Override
  public Map<String, Object> updateCardholder(long cardholderId) {
    return Map.of();
  }

  @NotNull
  private HttpUrl fetchEndpoint(String... fields) {
    var request = new Request.Builder().url(baseUrl + "/api").get().build();
    try (var response = httpClient.newCall(request).execute()) {
      if (response.code() == 200) {
        if (response.body() != null) {
          try {
            var typeRef = new TypeReference<Map<String, Object>>() {
            };
            var details = objectMapper.readValue(response.body().string(), typeRef);
            String endpointUrl = getNestedValue(details, fields);
            if (endpointUrl == null) {
              throw new NotFoundException("Unable to find Nested Value: " + Arrays.toString(fields));
            }
            var result = HttpUrl.parse(endpointUrl);
            if (result == null) {
              throw new ValidationException("Unable to parse endpoint: " + endpointUrl);
            }
            return result;
          } catch (JsonProcessingException jpe) {
            throw new ValidationException("Unable to parse response: " + jpe.getMessage(), jpe);
          }
        }
        throw new NotFoundException("Url response is missing: " + baseUrl + "/api");
      } else {
        throw new UnexpectedException("Unexpected response code: " + response.code());
      }
    } catch (IOException ioe) {
      log.info("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }
}
