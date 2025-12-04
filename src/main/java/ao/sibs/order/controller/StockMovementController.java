package ao.sibs.order.controller;

import ao.sibs.order.dto.StockMovementRequestDTO;
import ao.sibs.order.dto.StockMovementResponseDTO;
import ao.sibs.order.entity.StockMovement;
import ao.sibs.order.service.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stock-movements")
public class StockMovementController {

    private final StockMovementService stockMovementService;

    public StockMovementController(StockMovementService stockMovementService) {
        this.stockMovementService = stockMovementService;
    }

    @PostMapping
    public ResponseEntity<StockMovementResponseDTO> createStockMovement(@RequestBody StockMovementRequestDTO requestDTO) {
        StockMovementResponseDTO response = stockMovementService.createMovement(requestDTO);
                return ResponseEntity.status(201).body(response);
    }
}
