package re.edu.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import re.edu.orderservice.entity.Order;
import re.edu.orderservice.repository.OrderRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public Order createOrder(Order order) {
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        return orderRepository.save(order);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id).map(order -> {
            order.setCustomerId(orderDetails.getCustomerId());
            order.setTotalAmount(orderDetails.getTotalAmount());
            order.setStatus(orderDetails.getStatus());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }

    public void updateOrderStatus(Long id, String status) {
        orderRepository.findById(id).ifPresent(order -> {
            order.setStatus(status);
            orderRepository.save(order);
        });
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}

