package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.domain.item.Item;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {

    @Query("select r from Review r where r.item.id =:itemId")
    Optional<List<Review>> findAllByItemId(Long itemId);

    @Query("select r from Review r where r.item.brand.id =:brandId")
    Optional<List<Review>> findAllByBrandId(Long brandId);

    @Query("select r from Review r where r.customer.userId =:userId")
    Optional<List<Review>> findAllByUserId(Long userId);

    Optional<List<Review>> findAllByItem(Item item);

    @Query("select r from Review r where r.customer.userId=:userId and r.item.id=:itemId")
    Optional<Review> findByUserIdAndItemId(String userId, Long itemId);
}
