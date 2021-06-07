package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Category;

import java.util.List;
import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findCategoryByName(String name);

    List<Category> findAllByParentIsNull();
}
