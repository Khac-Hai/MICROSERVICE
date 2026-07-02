package com.example.pharmacykafka.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

	private final KafkaAdmin kafkaAdmin;

	@Autowired
	public TopicController(KafkaAdmin kafkaAdmin) {
		this.kafkaAdmin = kafkaAdmin;
	}

	@GetMapping
	public List<String> listTopics() {
		try (AdminClient adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties())) {
			ListTopicsResult result = adminClient.listTopics();
			Set<String> names = result.names().get();
			return names.stream().sorted().collect(Collectors.toList());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new RuntimeException("Interrupted while listing topics", e);
		} catch (ExecutionException e) {
			throw new RuntimeException("Failed to list topics", e.getCause());
		}
	}

}
