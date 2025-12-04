package ao.sibs.order.mappers;

import ao.sibs.order.dto.UserResponseDTO;
import ao.sibs.order.entity.User;

public class UserMapper {
    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId().toString());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}

