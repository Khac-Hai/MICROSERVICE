package com.example.order.service;

import com.example.order.dto.Product;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductClientService {

    private final DiscoveryClient discoveryClient;

    public ProductClientService(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
    }

    public Product getProductById(Long productId) {
        List<ServiceInstance> instances = discoveryClient.getInstances("PRODUCT-SERVICE");

        if (instances == null || instances.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "PRODUCT-SERVICE hiện không khả dụng"
            );
        }

        ServiceInstance productInstance = instances.get(0);
        String baseUrl = productInstance.getUri().toString();
        String targetUrl = baseUrl + "/api/v1/products/" + productId;

        WebClient client = WebClient.create();

        return client.get()
                .uri(targetUrl)
                .retrieve()
                .bodyToMono(Product.class)
                .block();
    }
}


