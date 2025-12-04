package ao.sibs.order.mappers;

import ao.sibs.order.dto.ItemResponseDTO;
import ao.sibs.order.dto.StockMovementRequestDTO;
import ao.sibs.order.dto.StockMovementResponseDTO;
import ao.sibs.order.entity.Item;
import ao.sibs.order.entity.StockMovement;

public class StockMovementMapper {
    public static StockMovement toEntity(StockMovementRequestDTO requestDTO, Item item) {
        StockMovement entity = new StockMovement();

        entity.setQuantity(requestDTO.getQuantity());
        entity.setItem(item);

        return entity;
    }
    public static StockMovementResponseDTO toResponseDTO(StockMovement stockMovement) {
        StockMovementResponseDTO dto = new StockMovementResponseDTO();

        dto.setId(stockMovement.getId() != null ? stockMovement.getId().toString() : null);
        dto.setQuantity(stockMovement.getQuantity());
        dto.setCreationDate(stockMovement.getCreationDate());

        if (stockMovement.getItem() != null) {
            ItemResponseDTO itemResponseDto = new ItemResponseDTO();
            itemResponseDto.setId(stockMovement.getItem().getId());

            dto.setItem(itemResponseDto);
        }

        return dto;
    }
    public static ItemResponseDTO toItemDTO(Item item) {
        if (item == null) {
            return null;
        }
        ItemResponseDTO itemResponseDto = new ItemResponseDTO();
        itemResponseDto.setId(item.getId());
        return itemResponseDto;
    }
}
