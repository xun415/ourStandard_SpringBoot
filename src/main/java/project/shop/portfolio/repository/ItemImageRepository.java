package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.item.ItemImage;
@Repository
public interface ItemImageRepository extends JpaRepository<ItemImage,Long> {
    //@Query("delete from ItemImage im where im.item.id =:itemId")
    //void deleteByItemId(Long itemId);
    @Query(value = "delete from item_image where item_item_id =:itemId",nativeQuery = true)
    void deleteByItemId(Long itemId);
}
