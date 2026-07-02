package com.example.notificationservice.service;

import com.example.pharmacykafka.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

	private final EmailService emailService;

	@Autowired
	public NotificationService(EmailService emailService) {
		this.emailService = emailService;
	}

	public void sendInvoice(OrderEvent event) {
		try {
			emailService.sendInvoiceEmail(event);
		} catch (Exception e) {
			log.error("Failed to send email for order {}: {}", 
					event.getOrderId(), e.getMessage());
		}

		log.info("====================================");
		log.info("Invoice for Order {} has been sent to customer.", event.getOrderId());
		log.info("====================================");
	}

}
