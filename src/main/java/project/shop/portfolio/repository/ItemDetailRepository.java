package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.item.ItemDetail;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDetailRepository extends JpaRepository<ItemDetail,Long> {
    @Query("select id from ItemDetail id where id.parentItem.id =:itemId")
    public Optional<List<ItemDetail>> findItemDetailByItemId(Long itemId);
}
