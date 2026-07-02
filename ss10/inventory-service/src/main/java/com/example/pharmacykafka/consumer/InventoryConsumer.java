package com.example.pharmacykafka.consumer;

import com.example.pharmacykafka.dto.OrderEvent;
import com.example.pharmacykafka.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryConsumer {

	private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

	private final InventoryService inventoryService;

	@Autowired
	public InventoryConsumer(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	@KafkaListener(topics = "medicine-stock-events", groupId = "inventory-group")
	public void consume(OrderEvent event) {
		log.info("=================================");
		log.info("Received Order Event");
		log.info("Order ID : {}", event.getOrderId());
		log.info("Medicine ID : {}", event.getMedicineId());
		log.info("Quantity : {}", event.getQuantity());
		log.info("Timestamp : {}", event.getTimestamp());
		log.info("=================================");

		try {
			inventoryService.updateInventory(event);
		} catch (Exception e) {
			log.error("Failed to update inventory for medicine id {}: {}",
					event.getMedicineId(), e.getMessage());
		}
	}

}
