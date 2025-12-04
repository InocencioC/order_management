package ao.sibs.order.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class StockMovementRequestDTO {
    private UUID itemId;
    private Integer quantity;
}
