package ao.sibs.order.service;

import ao.sibs.order.dto.ItemRequestDTO;
import ao.sibs.order.dto.ItemResponseDTO;
import ao.sibs.order.entity.Item;
import ao.sibs.order.mappers.ItemMapper;
import ao.sibs.order.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public ItemResponseDTO create(ItemRequestDTO itemRequestDTO) {
        log.info("Creating new Item: {}", itemRequestDTO.getName());

        Item itemToSave = ItemMapper.toEntity(itemRequestDTO);

        Item savedItem = itemRepository.save(itemToSave);

        return ItemMapper.toResponseDTO(savedItem);
    }

    @Transactional(readOnly = true)
    public Optional<Item> findById(UUID id){
        log.info("Reading Item: {}", id);
        return itemRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Item> readAll(){
        log.info("Reading all Items");
        return itemRepository.findAll();
    }

    public Item updateItem(UUID id, Item updatedItem) {
        log.info("Updating Item with id: {}", id);

        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found for updating with ID " + id));
        existingItem.setName(updatedItem.getName());
        return itemRepository.save(existingItem);
    }

    public void deleteItem(UUID id) {
        log.info("Deleting Item with id: {}", id);
        if (!itemRepository.existsById(id)) {
            throw new RuntimeException("Item not found for deleting with ID " + id);
        }
        itemRepository.deleteById(id);
    }

    public void deleteByName(String name) {
        log.info("Deleting Item with name: {}", name);
        itemRepository.deleteByName(name);
    }
}
