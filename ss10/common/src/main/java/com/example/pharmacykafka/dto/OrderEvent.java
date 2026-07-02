package com.example.pharmacykafka.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class OrderEvent {

	private Long orderId;
	private Long medicineId;
	private Integer quantity;
	private LocalDateTime timestamp;

	public OrderEvent() {
	}

	public OrderEvent(Long orderId, Long medicineId, Integer quantity, LocalDateTime timestamp) {
		this.orderId = orderId;
		this.medicineId = medicineId;
		this.quantity = quantity;
		this.timestamp = timestamp;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getMedicineId() {
		return medicineId;
	}

	public void setMedicineId(Long medicineId) {
		this.medicineId = medicineId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof OrderEvent)) {
			return false;
		}
		OrderEvent that = (OrderEvent) o;
		return Objects.equals(orderId, that.orderId)
				&& Objects.equals(medicineId, that.medicineId)
				&& Objects.equals(quantity, that.quantity)
				&& Objects.equals(timestamp, that.timestamp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, medicineId, quantity, timestamp);
	}

	@Override
	public String toString() {
		return "OrderEvent{" +
				"orderId=" + orderId +
				", medicineId=" + medicineId +
				", quantity=" + quantity +
				", timestamp=" + timestamp +
				'}';
	}

}
