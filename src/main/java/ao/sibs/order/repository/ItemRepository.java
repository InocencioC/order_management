package ao.sibs.order.repository;

import ao.sibs.order.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findByNameContainingIgnoreCase(String name);

    @Transactional
    @Modifying
    void deleteByName(String name);
}
