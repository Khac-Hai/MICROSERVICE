package re.edu.orderservice.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderRequestDTO {
    @NotNull
    private Long customerId;

    @NotNull
    private Long productId;

    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;
}

