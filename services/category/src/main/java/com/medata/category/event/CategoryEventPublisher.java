package com.medata.category.event;

import com.medata.category.dto.CategoryEventDto;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@Slf4j
public class CategoryEventPublisher {

  private final RestClient restClient;

  public CategoryEventPublisher(
      @LoadBalanced RestClient.Builder restClientBuilder,
      @Value("${labtest.base-url}") String labTestBaseUrl) {
    this.restClient = restClientBuilder.baseUrl(labTestBaseUrl).build();
  }

  public void publishCreated(UUID id, String name) {
    try {
      restClient
          .put()
          .uri("/internal/categories/{id}", id)
          .contentType(MediaType.APPLICATION_JSON)
          .body(new CategoryEventDto(name))
          .retrieve()
          .toBodilessEntity();
    } catch (RestClientException | IllegalStateException e) {
      log.warn("Could not publish category-created event for {}: {}", id, e.getMessage());
    }
  }

  public void publishDeleted(UUID id) {
    try {
      restClient.delete().uri("/internal/categories/{id}", id).retrieve().toBodilessEntity();
    } catch (RestClientException | IllegalStateException e) {
      log.warn("Could not publish category-deleted event for {}: {}", id, e.getMessage());
    }
  }
}
