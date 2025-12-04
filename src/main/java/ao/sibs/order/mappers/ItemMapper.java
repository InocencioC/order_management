package ao.sibs.order.mappers;

import ao.sibs.order.dto.ItemRequestDTO;
import ao.sibs.order.dto.ItemResponseDTO;
import ao.sibs.order.entity.Item;

public class ItemMapper {
    public static Item toEntity(ItemRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        Item item = new Item();
        item.setName(dto.getName());

        return item;
    }

    public static ItemResponseDTO toResponseDTO(Item item) {
        if (item == null) {
            return null;
        }

        ItemResponseDTO dto = new ItemResponseDTO();
        dto.setId(item.getId());
        dto.setName(item.getName());

        return dto;
    }
}
