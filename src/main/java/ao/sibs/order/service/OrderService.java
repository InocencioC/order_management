package ao.sibs.order.service;

import ao.sibs.order.dto.OrderRequestDTO;
import ao.sibs.order.dto.OrderResponseDTO;
import ao.sibs.order.entity.*;
import ao.sibs.order.mappers.OrderMapper;
import ao.sibs.order.repository.ItemRepository;
import ao.sibs.order.repository.OrderRepository;
import ao.sibs.order.repository.StockMovementRepository;
import ao.sibs.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private  final ItemRepository itemRepository;

    public Optional<OrderResponseDTO> findById(UUID id) {
        return orderRepository.findById(id)
                .map(OrderMapper::toResponseDTO);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO requestDTO) {

        User user = userRepository.findById(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + requestDTO.getUserId()));

        Item item = itemRepository.findById(requestDTO.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found with ID: " + requestDTO.getItemId()));

        Order order = OrderMapper.toEntity(user, item, requestDTO.getQuantity());

        order.setStatus(OrderStatus.PENDING);
        Order savedOrder = orderRepository.save(order);

        log.info("Order created with ID : {}", savedOrder.getId());
        tryToFulfillOrder(savedOrder);

        return OrderMapper.toResponseDTO(savedOrder);
    }

    private void tryToFulfillOrder(Order order) {
        if (order.getStatus() == OrderStatus.COMPLETED) {
            return;
        }
        UUID itemId = order.getItem().getId();
        Integer requiredQuantity = order.getQuantity() - order.getFulfilledQuantity();

        Integer currentStock = stockMovementRepository.getCurrentQuantity(itemId);

        if (currentStock >= requiredQuantity) {

            StockMovement stockMovement = new StockMovement();
            stockMovement.setItem(order.getItem());
            stockMovement.setQuantity(-requiredQuantity);
            stockMovement.setOrder(order);

            StockMovement savedMovement = stockMovementRepository.save(stockMovement);

            order.setFulfilledQuantity(order.getQuantity());
            order.setStatus(OrderStatus.COMPLETED);

            if (order.getAppliedMovements() == null) {
                order.setAppliedMovements(new ArrayList<>());
            }
            order.getAppliedMovements().add(savedMovement);

            orderRepository.save(order);

            log.info("ORDER COMPLETED (ID: {}). Movement applied: {}", order.getId(), savedMovement.getId());

            notificationService.sendOrderCompleteEmail(order.getUser(), order);
        } else {
            log.warn("Order ID {} cannot be completed. Required: {}, Available: {}",
                    order.getId(), requiredQuantity, currentStock);
        }
    }


    public String getCompletionStatus(UUID orderId) {
        OrderResponseDTO order = findById(orderId)
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
