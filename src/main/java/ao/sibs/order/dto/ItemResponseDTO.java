package ao.sibs.order.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemResponseDTO {
    private UUID id;
    private String name;
}
