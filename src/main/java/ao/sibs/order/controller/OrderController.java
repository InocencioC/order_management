package ao.sibs.order.controller;

import ao.sibs.order.dto.OrderRequestDTO;
import ao.sibs.order.dto.OrderResponseDTO;
import ao.sibs.order.entity.Order;
import ao.sibs.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO order) {
        OrderResponseDTO createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(201).body(createdOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable UUID id) {
        return orderService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/completion")
    public ResponseEntity<String> getOrderCompletion(@PathVariable UUID id) {
        return orderService.findById(id)
                .map(order -> {
                    double completionPercentage =
                            ((double) order.getFulfilledQuantity() / order.getQuantity()) * 100;
                    String statusReport = String.format(
                            "Order %s Status: %s. Fulfilled: %d/%d (%.2f%%)",
                            order.getId(), order.getStatus(),
                            order.getFulfilledQuantity(), order.getQuantity(),
                            completionPercentage
                    );
                    return ResponseEntity.ok(statusReport);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.findAll();
    }
}