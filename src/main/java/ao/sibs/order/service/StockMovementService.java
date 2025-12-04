package ao.sibs.order.service;

import ao.sibs.order.entity.StockMovement;
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

    @Transactional
    public StockMovement createMovement(StockMovement stockMovement) {
        StockMovement savedMovement = stockMovementRepository.save(stockMovement);

        log.info("Stock Movement created: ID={}, Item  ID={}, Quantity={}",
                savedMovement.getId(), savedMovement.getItem().getId(), savedMovement.getQuantity());

        if (savedMovement.getQuantity() > 0) {
            log.info("--- Stock Movement positive. Triggering order fulfillment check. ---");

            orderService.tryToFulfillPendingOrders(savedMovement.getItem().getId());
        }
        return savedMovement;
    }
    public StockMovement read(UUID id) {
        return stockMovementRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stock Movement not found"));
    }
}