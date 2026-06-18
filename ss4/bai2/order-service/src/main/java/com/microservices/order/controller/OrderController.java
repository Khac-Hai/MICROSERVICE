package com.microservices.order.controller;

import com.microservices.order.entity.Order;
import com.microservices.order.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Optional<Order> order = orderRepository.findById(id);
        return order.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.ok(savedOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        Optional<Order> order = orderRepository.findById(id);
        if (order.isPresent()) {
            Order ord = order.get();
            if (orderDetails.getCustomerId() != null) {
                ord.setCustomerId(orderDetails.getCustomerId());
            }
            if (orderDetails.getProductId() != null) {
                ord.setProductId(orderDetails.getProductId());
            }
            if (orderDetails.getQuantity() != null) {
                ord.setQuantity(orderDetails.getQuantity());
            }
            if (orderDetails.getTotalPrice() != null) {
                ord.setTotalPrice(orderDetails.getTotalPrice());
            }
            if (orderDetails.getStatus() != null) {
                ord.setStatus(orderDetails.getStatus());
            }
            if (orderDetails.getNotes() != null) {
                ord.setNotes(orderDetails.getNotes());
            }
            return ResponseEntity.ok(orderRepository.save(ord));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        if (orderRepository.existsById(id)) {
            orderRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}

