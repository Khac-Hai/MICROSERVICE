package com.example.pharmacykafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

	@Bean
	public NewTopic medicineStockEventsTopic() {
		return TopicBuilder.name("medicine-stock-events")
				.partitions(3)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic medicinePriceUpdatesTopic() {
		return TopicBuilder.name("medicine-price-updates")
				.partitions(1)
				.replicas(1)
				.build();
	}

	@Bean
	public NewTopic pharmacyNotificationsTopic() {
		return TopicBuilder.name("pharmacy-notifications")
				.partitions(2)
				.replicas(1)
				.build();
	}

}
