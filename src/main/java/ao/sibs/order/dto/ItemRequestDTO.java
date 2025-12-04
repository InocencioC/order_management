package ao.sibs.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ItemRequestDTO {
    @NotBlank(message = "The name is required.")
    private String name;

}
