package com.example.order.controller;

import com.example.order.dto.Product;
import com.example.order.service.ProductClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final ProductClientService productClientService;

    public OrderController(ProductClientService productClientService) {
        this.productClientService = productClientService;
    }

    @GetMapping("/getProduct/{id}")
    public ResponseEntity<Product> getProductFromProductService(@PathVariable("id") Long id) {
        Product p = productClientService.getProductById(id);
        return ResponseEntity.ok(p);
    }
}


