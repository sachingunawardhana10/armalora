package com.armalora.product.client;

import com.armalora.product.dto.InventoryResponse;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient() {

        this.restClient =
                RestClient.builder()
                        .baseUrl(
                                "http://localhost:8082"
                        )
                        .build();
    }

    public List<InventoryResponse>
    getInventoryByProductId(
            Long productId) {

        return restClient
                .get()
                .uri(
                        "/api/inventory/product/{productId}",
                        productId
                )
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(
                        new ParameterizedTypeReference<
                                List<InventoryResponse>>() {}
                );
    }
}