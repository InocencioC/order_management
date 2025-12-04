package ao.sibs.order.mappers;

import ao.sibs.order.dto.OrderResponseDTO;
import ao.sibs.order.entity.Item;
import ao.sibs.order.entity.Order;
import ao.sibs.order.entity.User;
import lombok.Data;

@Data
public class OrderMapper {
    public static OrderResponseDTO toResponseDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setCreated(order.getCreated());
        dto.setQuantity(order.getQuantity());
        dto.setFulfilledQuantity(order.getFulfilledQuantity());
        dto.setStatus(order.getStatus());

        if (order.getUser() != null) {
            dto.setUser(UserMapper.toResponseDTO(order.getUser()));
        }
        if (order.getItem() != null) {
            dto.setItem(StockMovementMapper.toItemDTO(order.getItem()));
        }

        return dto;
    }

    public static Order toEntity(User user, Item item, Integer quantity) {
        Order order = new Order();
        order.setUser(user);
        order.setItem(item);
        order.setQuantity(quantity);
        return order;
    }
}

