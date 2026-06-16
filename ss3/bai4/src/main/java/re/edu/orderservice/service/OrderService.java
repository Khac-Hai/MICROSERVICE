package re.edu.orderservice.service;

import re.edu.orderservice.dto.OrderRequestDTO;
import re.edu.orderservice.dto.OrderResponseDTO;

public interface OrderService {
    OrderResponseDTO createOrder(OrderRequestDTO request);

    OrderResponseDTO getOrderById(Long id);
}

