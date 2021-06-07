package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Order;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query("select o from Order o where o.customer.id =:userId")
    List<Order> findAllByUserId(Long userId);



//    @Query(value = "SELECT o.order_id FROM orders o, order_item oi, item_detail detail,item " +
//            "WHERE oi.order_id = o.order_id " +
//            "AND oi.item_detail_id = detail.item_detail_id AND detail.item_id =item.item_id " +
//            "AND item.brand_id = :brandId",nativeQuery = true)
    @Query(value = "select order_id from brandOrders where brandOrders.brand_id =:brandId",nativeQuery = true)
    List<Object[]> findIdByBrandId(@Param("brandId") Long brandId);
}
