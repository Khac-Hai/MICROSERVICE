package com.microservice.orderservice.controller;

import com.microservice.orderservice.dto.ProductDTO;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        Order order = orderService.createOrder(productId, quantity);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrder(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.findAll());
    }

    @GetMapping("/check-product/{productId}")
    public ResponseEntity<ProductDTO> checkProductService(@PathVariable Long productId) {
        ProductDTO product = orderService.getProduct(productId);
        return ResponseEntity.ok(product);
    }

}

