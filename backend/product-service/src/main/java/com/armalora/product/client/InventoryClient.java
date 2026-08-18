package com.armalora.product.client;

import com.armalora.product.dto.InventoryResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

@Component
public class InventoryClient {

    private final RestClient restClient;

    public InventoryClient(
            @Value("${inventory.service.url}")
            String inventoryServiceUrl) {

        this.restClient =
                RestClient.builder()
                        .baseUrl(inventoryServiceUrl)
                        .build();
    }

    public List<InventoryResponse>
    getInventoryByProductId(Long productId) {

        try {

            List<InventoryResponse> response =
                    restClient
                            .get()
                            .uri(
                                    "/api/inventory/product/{productId}",
                                    productId
                            )
                            .accept(
                                    MediaType.APPLICATION_JSON
                            )
                            .retrieve()
                            .body(
                                    new ParameterizedTypeReference<
                                            List<InventoryResponse>>() {}
                            );

            if (response == null) {
                return Collections.emptyList();
            }

            return response;

        } catch (Exception exception) {

            return Collections.emptyList();
        }
    }
}