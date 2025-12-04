package ao.sibs.order.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class OrderRequestDTO {

    @NotNull(message = "O ID do utilizador é obrigatório.")
    private UUID userId;

    @NotNull(message = "O ID do item é obrigatório.")
    private UUID itemId;

    @Min(value = 1, message = "A quantidade deve ser pelo menos 1.")
    private Integer quantity;
}
