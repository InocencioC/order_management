package ao.sibs.order.service;

import ao.sibs.order.entity.Order;
import ao.sibs.order.entity.OrderStatus;
import ao.sibs.order.entity.StockMovement;
import ao.sibs.order.repository.OrderRepository;
import ao.sibs.order.repository.StockMovementRepository;
import ao.sibs.order.repository.UserRepository;
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
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final StockMovementRepository stockMovementRepository;
    private final NotificationService notificationService;

    public Optional<Order> findById(UUID id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        log.info("Order created with ID : {}", savedOrder.getId());
        tryToFulfillOrder(savedOrder);
        return savedOrder;
    }

    private void tryToFulfillOrder(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            return;
        }
        UUID itemId = order.getItem().getId();
        Integer requiredQuantity = order.getQuantity() - order.getFulfilledQuantity();

        Integer currentStock = stockMovementRepository.getCurrentQuantity(itemId);

        if (currentStock >= requiredQuantity) {
            StockMovement stockMovement = stockMovementRepository.findById(itemId).get();
            stockMovement.setItem(order.getItem());
            stockMovement.setQuantity(-requiredQuantity);
            stockMovement.setOrder(order);

            StockMovement savedMovement = stockMovementRepository.save(stockMovement);

            order.setFulfilledQuantity(order.getQuantity());
            order.setStatus(OrderStatus.COMPLETED);

            order.getAppliedMovements().add(stockMovement);
            orderRepository.save(order);

            log.info("ORDER COMPLETED (ID: {}). Movement applied: {}", order.getId(), savedMovement.getId());

            notificationService.sendOrderCompleteEmail(order.getUser(), order);
        } else {
            log.warn("Order ID {} cannot be completed. Required: {}, Available: {}",
                    order.getId(), requiredQuantity, currentStock);
        }
    }


    public String getCompletionStatus(UUID orderId) {
        Order order = findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        double completionRate = (double) order.getFulfilledQuantity() / order.getQuantity();
        return String.format(
                "Order %s is %.2f%% complete. Fulfilled: %d/%d. Status: %s",
                order.getId(), completionRate * 100, order.getFulfilledQuantity(),
                order.getQuantity(), order.getStatus());
    }

    public void tryToFulfillPendingOrders(UUID itemId) {
        List<Order> pendingOrders = orderRepository.findByItemIdAndStatus(itemId, OrderStatus.PENDING);

        if (pendingOrders.isEmpty()) {
            log.info("No pending orders found for Item ID: {}", itemId);
            return;
        }

        for (Order order : pendingOrders) {
            tryToFulfillOrder(order);
        }
    }


}
