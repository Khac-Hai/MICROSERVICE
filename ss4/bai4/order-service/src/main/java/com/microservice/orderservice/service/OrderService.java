package com.microservice.orderservice.service;

import com.microservice.orderservice.dto.ProductDTO;
import com.microservice.orderservice.entity.Order;
import com.microservice.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public ProductDTO getProduct(Long productId) {
        // Sử dụng Service Name thay vì localhost:port
        String url = "http://PRODUCT-SERVICE/api/v1/products/" + productId;
        
        return restTemplate.getForObject(
                url,
                ProductDTO.class
        );
    }

    public Order createOrder(Long productId, Integer quantity) {
        // Gọi Product Service để lấy thông tin sản phẩm
        ProductDTO product = getProduct(productId);

        // Tạo đơn hàng
        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotalPrice(product.getPrice() * quantity);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("SUCCESS");
        order.setProductName(product.getName());

        return orderRepository.save(order);
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

}

