package com.example.pharmacykafka.controller;

import com.example.pharmacykafka.dto.OrderEvent;
import com.example.pharmacykafka.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

	private static final String TOPIC = "medicine-stock-events";

	private final OrderService orderService;

	@Autowired
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping("/sell")
	public ResponseEntity<Map<String, Object>> sellMedicine(@RequestBody OrderEvent event) {
		String message = orderService.sellMedicine(event);

		Map<String, Object> response = new LinkedHashMap<>();
		response.put("message", message);
		response.put("topic", TOPIC);
		response.put("medicineId", event.getMedicineId());
		response.put("partitionKey", String.valueOf(event.getMedicineId()));

		return ResponseEntity.ok(response);
	}

}
