package ao.sibs.order.dto;

import ao.sibs.order.entity.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderResponseDTO {
    private UUID id;
    private LocalDateTime created;
    private Integer quantity;
    private Integer fulfilledQuantity;
    private OrderStatus status;

    private UserResponseDTO user;
    private ItemResponseDTO item;
}
