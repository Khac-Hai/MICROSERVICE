package com.example.pharmacykafka.producer;

import com.example.pharmacykafka.dto.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderProducer {

	private static final String TOPIC = "medicine-stock-events";

	private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

	@Autowired
	public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendOrder(OrderEvent event) {
		String partitionKey = String.valueOf(event.getMedicineId());
		kafkaTemplate.send(TOPIC, partitionKey, event);
	}

}
