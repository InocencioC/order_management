package ao.sibs.order.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockMovementResponseDTO {
    private String id;
    private LocalDateTime creationDate;
    private Integer quantity;

    private ItemResponseDTO item;
    private OrderDTO order;

}
