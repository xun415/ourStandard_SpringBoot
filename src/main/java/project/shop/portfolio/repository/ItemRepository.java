package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.item.Item;
@Repository
public interface ItemRepository extends JpaRepository<Item,Long> , QuerydslPredicateExecutor<Item> {
}
