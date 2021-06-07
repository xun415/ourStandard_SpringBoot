package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Delivery;
@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
}
