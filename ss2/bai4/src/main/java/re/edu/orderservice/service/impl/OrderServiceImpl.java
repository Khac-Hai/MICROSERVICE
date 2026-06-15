package re.edu.orderservice.service.impl;

import org.springframework.stereotype.Service;
import re.edu.orderservice.entity.Order;
import re.edu.orderservice.exception.ResourceNotFoundException;
import re.edu.orderservice.repository.OrderRepository;
import re.edu.orderservice.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order với ID " + id + " không tồn tại"));
    }
}

