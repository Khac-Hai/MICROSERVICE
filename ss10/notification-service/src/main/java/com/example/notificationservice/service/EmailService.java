package com.example.notificationservice.service;

import com.example.pharmacykafka.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private static final Logger log = LoggerFactory.getLogger(EmailService.class);

	private final JavaMailSender mailSender;

	@Value("${spring.mail.enabled:false}")
	private boolean mailEnabled;

	@Value("${spring.mail.username:notification@pharmacy.local}")
	private String fromEmail;

	@Autowired
	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendInvoiceEmail(OrderEvent event) {
		try {
			if (!mailEnabled) {
				log.info("Mail service disabled. Simulating email send for Order {}", 
						event.getOrderId());
				return;
			}

			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(fromEmail);
			message.setTo("customer@pharmacy.local");
			message.setSubject("Medicine Invoice");
			message.setText(buildInvoiceBody(event));

			mailSender.send(message);
			log.info("Invoice email sent for Order {}", event.getOrderId());
		} catch (Exception e) {
			log.warn("Could not send email: {}. Continuing in mock mode.", e.getMessage());
		}
	}

	private String buildInvoiceBody(OrderEvent event) {
		return String.format(
				"Order Invoice\n" +
				"==============\n" +
				"Order ID: %d\n" +
				"Medicine ID: %d\n" +
				"Quantity: %d\n" +
				"Timestamp: %s\n",
				event.getOrderId(),
				event.getMedicineId(),
				event.getQuantity(),
				event.getTimestamp()
		);
	}

}
