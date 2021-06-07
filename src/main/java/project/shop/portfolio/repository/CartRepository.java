package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Cart;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {
    @Query("select c from Cart c where c.customer.userId =:userId")
    Optional<List<Cart>> findAllByUserId(String userId);


    void deleteById(Long id);
}
