package com.filmrental.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BackendGateway {

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() { };
    private static final TypeReference<List<Map<String, Object>>> LIST_TYPE = new TypeReference<>() { };

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public BackendGateway(
            RestClient.Builder restClientBuilder,
            ObjectMapper objectMapper,
            @Value("${frontend.backend.base-url}") String baseUrl) {
        this.restClient = restClientBuilder.build();
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
    }

    public List<Map<String, Object>> getTopRentedFilms() {
        return getList("/api/v1/films/top-rented");
    }

    public List<Map<String, Object>> getFilmsByCategory(Integer categoryId) {
        return getList("/api/v1/films/category/" + categoryId);
    }

    public List<Map<String, Object>> getFilmsByActor(Integer actorId) {
        return getList("/api/v1/films/actor/" + actorId);
    }

    public Map<String, Object> getFilm(Integer filmId) {
        return getMap("/api/v1/films/" + filmId);
    }

    public List<Map<String, Object>> getActorFilms(Integer actorId) {
        return getList("/api/v1/actors/" + actorId + "/films");
    }

    public Map<String, Object> getCustomer(Integer customerId) {
        return getMap("/api/v1/customers/" + customerId);
    }

    public List<Map<String, Object>> getCustomerRentals(Integer customerId) {
        return getList("/api/v1/customers/" + customerId + "/rentals");
    }

    public List<Map<String, Object>> getCustomerPayments(Integer customerId) {
        return getList("/api/v1/customers/" + customerId + "/payments");
    }

    public List<Map<String, Object>> getRentalsByCustomer(Integer customerId) {
        return getList("/api/v1/rentals/customer/" + customerId);
    }

    public Map<String, Object> getStore(Integer storeId) {
        return getMap("/api/v1/stores/" + storeId);
    }

    public Map<String, Object> getStoreByManager(Integer managerStaffId) {
        return getMap("/api/v1/stores/manager/" + managerStaffId);
    }

    public List<Map<String, Object>> getStoreInventory(Integer storeId) {
        return getList("/api/v1/stores/" + storeId + "/inventory");
    }

    public Map<String, Object> getInventory(Integer inventoryId) {
        return getMap("/api/v1/inventory/" + inventoryId);
    }

    public List<Map<String, Object>> getInventoryByStore(Integer storeId) {
        return getList("/api/v1/inventory/store/" + storeId);
    }

    public List<Map<String, Object>> getInventoryByFilm(Integer filmId) {
        return getList("/api/v1/inventory/film/" + filmId);
    }

    private Map<String, Object> getMap(String path) {
        Object body = getBody(path);
        if (body == null) {
            return Collections.emptyMap();
        }
        return objectMapper.convertValue(body, MAP_TYPE);
    }

    private List<Map<String, Object>> getList(String path) {
        Object body = getBody(path);
        if (body == null) {
            return Collections.emptyList();
        }
        return objectMapper.convertValue(body, LIST_TYPE);
    }

    private Object getBody(String path) {
        try {
            return request(path)
                    .retrieve()
                    .body(Object.class);
        } catch (RestClientException ex) {
            throw new BackendGatewayException(ex.getMessage(), ex);
        }
    }

    private RestClient.RequestHeadersSpec<?> request(String path) {
        return restClient.get()
                .uri(baseUrl + path)
                .accept(MediaType.APPLICATION_JSON);
    }

    public static class BackendGatewayException extends RuntimeException {
        public BackendGatewayException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
