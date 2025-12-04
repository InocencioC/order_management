package ao.sibs.order.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderDTO {
    private String id;
    private LocalDateTime created;
    private Integer quantity;
    private Integer fulfilledQuantity;
    private String status;
}
