package ao.sibs.order.service;

import ao.sibs.order.dto.StockMovementRequestDTO;
import ao.sibs.order.dto.StockMovementResponseDTO;
import ao.sibs.order.entity.Item;
import ao.sibs.order.entity.StockMovement;
import ao.sibs.order.mappers.StockMovementMapper;
import ao.sibs.order.repository.ItemRepository; // <-- NOVO IMPORT NECESSÁRIO
import ao.sibs.order.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockMovementService {

    private final StockMovementRepository stockMovementRepository;
    private final OrderService orderService;
    private final ItemRepository itemRepository; // <-- NOVO: Injeção do ItemRepository

    @Transactional
    public StockMovementResponseDTO createMovement(StockMovementRequestDTO requestDTO) {

        Item item = itemRepository.findById(requestDTO.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        StockMovement stockMovement = StockMovementMapper.toEntity(requestDTO, item);
        StockMovement savedMovement = stockMovementRepository.save(stockMovement);
        log.info("Stock Movement created: ID={}, Item ID={}, Quantity={}",
                savedMovement.getId(), savedMovement.getItem().getId(), savedMovement.getQuantity());

        if (savedMovement.getQuantity() > 0) {
            log.info("--- Stock Movement positivo. A verificar satisfação de pedidos pendentes. ---");
            orderService.tryToFulfillPendingOrders(savedMovement.getItem().getId());
        }

        return StockMovementMapper.toResponseDTO(savedMovement);
    }

    public StockMovement read(UUID id) {
        return stockMovementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Movement not found"));
    }
}