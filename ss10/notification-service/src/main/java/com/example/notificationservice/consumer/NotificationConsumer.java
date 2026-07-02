package com.example.notificationservice.consumer;

import com.example.pharmacykafka.dto.OrderEvent;
import com.example.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {

	private static final Logger log = LoggerFactory.getLogger(NotificationConsumer.class);

	private final NotificationService notificationService;

	@Autowired
	public NotificationConsumer(NotificationService notificationService) {
		this.notificationService = notificationService;
	}

	@KafkaListener(topics = "medicine-stock-events", groupId = "notification-group")
	public void consume(OrderEvent event) {
		log.info("====================================");
		log.info("Notification Service");
		log.info("Received Order Event");
		log.info("Order ID : {}", event.getOrderId());
		log.info("Medicine ID : {}", event.getMedicineId());
		log.info("Quantity : {}", event.getQuantity());
		log.info("Timestamp : {}", event.getTimestamp());
		log.info("====================================");

		try {
			notificationService.sendInvoice(event);
		} catch (Exception e) {
			log.error("Failed to process notification for order {}: {}",
					event.getOrderId(), e.getMessage());
		}
	}

}
