package com.example.pharmacykafka.service;

import com.example.pharmacykafka.dto.OrderEvent;
import com.example.pharmacykafka.entity.Medicine;
import com.example.pharmacykafka.repository.MedicineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {

	private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

	private final MedicineRepository medicineRepository;

	@Autowired
	public InventoryService(MedicineRepository medicineRepository) {
		this.medicineRepository = medicineRepository;
	}

	@Transactional
	public void updateInventory(OrderEvent event) {
		Medicine medicine = medicineRepository.findById(event.getMedicineId())
				.orElseThrow(() -> new RuntimeException(
						"Medicine not found with id " + event.getMedicineId()));

		if (medicine.getStock() < event.getQuantity()) {
			log.error("Insufficient Stock");
			throw new RuntimeException(
					"Insufficient stock for medicine id " + event.getMedicineId());
		}

		medicine.setStock(medicine.getStock() - event.getQuantity());
		medicineRepository.save(medicine);

		log.info("=================================");
		log.info("Inventory Updated");
		log.info("Medicine ID : {}", medicine.getId());
		log.info("Quantity : {}", event.getQuantity());
		log.info("Current Stock : {}", medicine.getStock());
		log.info("=================================");
	}

}
