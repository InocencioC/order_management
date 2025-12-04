package ao.sibs.order.service;

import ao.sibs.order.dto.UserUpdateDTO;
import ao.sibs.order.entity.User;
import ao.sibs.order.exception.ResourceNotFoundException;
import ao.sibs.order.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User create(User user) {
        return userRepository.save(user);
    }
    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public User update(UUID id, UserUpdateDTO dto) {

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        if (dto.getEmail() != null &&
                userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
            throw new IllegalArgumentException(
                    "Email already in use by another user: " + dto.getEmail());
        }

        existingUser.setName(dto.getName());
        existingUser.setEmail(dto.getEmail());

        return userRepository.save(existingUser);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
