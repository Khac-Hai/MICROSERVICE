package com.example.pharmacykafka.service;

import com.example.pharmacykafka.dto.OrderEvent;
import com.example.pharmacykafka.producer.OrderProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

	private final OrderProducer orderProducer;

	@Autowired
	public OrderService(OrderProducer orderProducer) {
		this.orderProducer = orderProducer;
	}

	public String sellMedicine(OrderEvent event) {
		event.setTimestamp(LocalDateTime.now());
		try {
			orderProducer.sendOrder(event);
		} catch (Exception e) {
			throw new IllegalStateException("Failed to send medicine order event", e);
		}
		return "Medicine order sent successfully";
	}

}
