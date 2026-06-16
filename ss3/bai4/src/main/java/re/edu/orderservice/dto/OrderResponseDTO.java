package re.edu.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Long customerId;
    private Long productId;
    private Integer quantity;
    private LocalDateTime orderDate;
    private Double totalAmount;
}

