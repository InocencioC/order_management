package ao.sibs.order.repository;

import ao.sibs.order.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StockMovementRepository extends JpaRepository<StockMovement, UUID> {

    List<StockMovement> findByItemIdOrderByCreationDateDesc(UUID itemId);
    List<StockMovement> findByOrderId(UUID orderId);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM StockMovement s WHERE s.item.id = :itemId")
    Integer getCurrentQuantity(@Param("itemId") UUID itemId);
}
