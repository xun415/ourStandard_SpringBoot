package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.OrderItem;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
