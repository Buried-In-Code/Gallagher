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
import github.buriedincode.gallagher.models.*;
import java.io.IOException;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
  public void addCardToCardholder(long cardholderId, @NotNull AddCardRequest newCard) {
    var url = fetchEndpoint("features", "cardholders", "cardholders", "href").newBuilder()
        .addPathSegment(String.valueOf(cardholderId)).build();

    String requestBody;
    try {
      requestBody = objectMapper.writeValueAsString(newCard);
    } catch (JsonProcessingException jpe) {
      throw new ValidationException("Unable to serialize object: %s".formatted(jpe.getMessage()), jpe);
    }
    var request = new Request.Builder().url(url)
        .patch(RequestBody.create(requestBody, MediaType.get("application/json"))).build();

    try (var response = httpClient.newCall(request).execute()) {
      if (response.code() == 200 || response.code() == 204)
        return;
      var responseBody = response.body() != null ? response.body().string() : "";
      switch (response.code()) {
        case 400 -> throw new BadRequestException(parseErrorMessage(responseBody));
        case 403 -> throw new ForbiddenException(parseErrorMessage(responseBody));
        case 409 -> throw new ConflictException(parseErrorMessage(responseBody));
        default -> throw new UnexpectedException(
            "Unexpected response code: " + response.code() + " : " + parseErrorMessage(responseBody));
      }
    } catch (IOException ioe) {
      log.error("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @Override
  public void createCardholder(@NotNull UserRequest newUser) {
    var url = fetchEndpoint("features", "cardholders", "cardholders", "href");

    String requestBody;
    try {
      requestBody = objectMapper.writeValueAsString(newUser);
    } catch (JsonProcessingException jpe) {
      throw new ValidationException("Unable to serialize object: %s".formatted(jpe.getMessage()), jpe);
    }
    var request = new Request.Builder().url(url)
        .post(RequestBody.create(requestBody, MediaType.get("application/json"))).build();

    try (var response = httpClient.newCall(request).execute()) {
      if (response.code() == 201)
        return;
      var responseBody = response.body() != null ? response.body().string() : "";
      switch (response.code()) {
        case 400 -> throw new BadRequestException(parseErrorMessage(responseBody));
        case 403 -> throw new ForbiddenException(parseErrorMessage(responseBody));
        default -> throw new UnexpectedException(
            "Unexpected response code: " + response.code() + " : " + parseErrorMessage(responseBody));
      }
    } catch (IOException ioe) {
      log.error("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @Override
  public void deleteCardholder(long cardholderId) {
    var url = fetchEndpoint("features", "cardholders", "cardholders", "href").newBuilder()
        .addPathSegment(String.valueOf(cardholderId)).build();
    var request = new Request.Builder().url(url).delete().build();

    try (var response = httpClient.newCall(request).execute()) {
      if (response.code() == 200 || response.code() == 204)
        return;
      var responseBody = response.body() != null ? response.body().string() : "";
      switch (response.code()) {
        case 400 -> throw new BadRequestException(parseErrorMessage(responseBody));
        case 403 -> throw new ForbiddenException(parseErrorMessage(responseBody));
        case 404 -> throw new NotFoundException(parseErrorMessage(responseBody));
        case 409 -> throw new ConflictException(parseErrorMessage(responseBody));
        default -> throw new UnexpectedException(
            "Unexpected response code: " + response.code() + " : " + parseErrorMessage(responseBody));
      }
    } catch (IOException ioe) {
      log.error("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @NotNull
  @Override
  public Cardholder getCardholder(long cardholderId) {
    var url = fetchEndpoint("features", "cardholders", "cardholders", "href").newBuilder()
        .addPathSegment(String.valueOf(cardholderId)).build();
    var request = new Request.Builder().url(url).get().build();

    try (var response = httpClient.newCall(request).execute()) {
      var responseBody = response.body() != null ? response.body().string() : "";
      return switch (response.code()) {
        case 200 -> objectMapper.readValue(responseBody, Cardholder.class);
        case 404 -> throw new NotFoundException(parseErrorMessage(responseBody));
        default -> throw new UnexpectedException(
            "Unexpected response: " + response.code() + " - " + parseErrorMessage(responseBody));
      };
    } catch (IOException ioe) {
      log.error("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @Nullable
  @Override
  public CardholderSummary searchCardholder(@NotNull String email) {
    var emailPdf = searchPDF("email");
    if (emailPdf == null) {
      return null;
    }
    var emailId = emailPdf.id();

    var url = fetchEndpoint("features", "cardholders", "cardholders", "href").newBuilder()
        .addEncodedQueryParameter("pdf_" + emailId, "\"%s\"".formatted(email))
        .addQueryParameter("fields", "defaults,personalDataFields,cards").build();
    var request = new Request.Builder().url(url).get().build();

    try (var response = httpClient.newCall(request).execute()) {
      var responseBody = response.body() != null ? response.body().string() : "";
      switch (response.code()) {
        case 200 -> {
          var details = objectMapper
              .readValue(responseBody, new TypeReference<SearchResponse<CardholderSummary>>() {
              }).results();
          return details.isEmpty() ? null : details.getFirst();
        }
        case 400 -> throw new BadRequestException(parseErrorMessage(responseBody));
        case 403 -> throw new ForbiddenException(parseErrorMessage(responseBody));
        default -> throw new UnexpectedException(
            "Unexpected response code: " + response.code() + " : " + parseErrorMessage(responseBody));
      }
    } catch (IOException ioe) {
      log.error("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @Nullable
  @Override
  public PersonalDataFieldSummary searchPDF(@NotNull String name) {
    var url = fetchEndpoint("features", "personalDataFields", "personalDataFields", "href").newBuilder()
        .addQueryParameter("name", name).build();
    var request = new Request.Builder().url(url).get().build();

    try (var response = httpClient.newCall(request).execute()) {
      var responseBody = response.body() != null ? response.body().string() : "";
      switch (response.code()) {
        case 200 -> {
          var details = objectMapper
              .readValue(responseBody, new TypeReference<SearchResponse<PersonalDataFieldSummary>>() {
              }).results();
          return details.isEmpty() ? null : details.getFirst();
        }
        case 403 -> throw new ForbiddenException(parseErrorMessage(responseBody));
        default -> throw new UnexpectedException(
            "Unexpected response: " + response.code() + " - " + parseErrorMessage(responseBody));
      }
    } catch (IOException ioe) {
      log.error("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @NotNull
  private HttpUrl fetchEndpoint(String... fields) {
    var request = new Request.Builder().url(baseUrl + "/api").get().build();

    try (var response = httpClient.newCall(request).execute()) {
      var responseBody = response.body() != null ? response.body().string() : "";

      try {
        var details = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {
        });
        var endpointUrl = (String) Optional.ofNullable(getNestedValue(details, fields)).orElseThrow(
            () -> new NotFoundException("Unable to find nested value: " + Arrays.toString(fields)));
        return Optional.ofNullable(HttpUrl.parse(endpointUrl))
            .orElseThrow(() -> new ValidationException("Unable to parse endpoint: " + endpointUrl));
      } catch (JsonProcessingException jpe) {
        throw new ValidationException("Unable to parse response: " + jpe.getMessage(), jpe);
      }
    } catch (IOException ioe) {
      log.error("Error calling {}: {}", request.url(), ioe.getMessage());
      throw new UnexpectedException(ioe.getMessage(), ioe);
    }
  }

  @NotNull
  private String parseErrorMessage(String responseBody) throws IOException {
    return objectMapper.readValue(responseBody, new TypeReference<Map<String, String>>() {
    }).getOrDefault("message", "Unknown error");
  }
}
