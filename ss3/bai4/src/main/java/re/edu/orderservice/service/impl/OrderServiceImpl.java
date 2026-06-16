package re.edu.orderservice.service.impl;

import org.springframework.stereotype.Service;
import re.edu.orderservice.dto.OrderRequestDTO;
import re.edu.orderservice.dto.OrderResponseDTO;
import re.edu.orderservice.entity.Order;
import re.edu.orderservice.exception.ResourceNotFoundException;
import re.edu.orderservice.repository.OrderRepository;
import re.edu.orderservice.service.OrderService;

import java.time.LocalDateTime;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO request) {
        // In this exercise product price is mocked as 100.0
        double productPrice = 100.0;
        double total = productPrice * request.getQuantity();

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .orderDate(LocalDateTime.now())
                .totalAmount(total)
                .build();

        Order saved = orderRepository.save(order);

        return mapToDto(saved);
    }

    @Override
    public OrderResponseDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        return mapToDto(order);
    }

    private OrderResponseDTO mapToDto(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getCustomerId(),
                order.getProductId(),
                order.getQuantity(),
                order.getOrderDate(),
                order.getTotalAmount()
        );
    }
}

