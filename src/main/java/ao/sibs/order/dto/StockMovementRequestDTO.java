package ao.sibs.order.dto;

import lombok.Data;

@Data
public class StockMovementRequestDTO {
    private String itemId;
    private Integer quantity;
}
