package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Comments;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentsRepository extends JpaRepository<Comments,Long> {
    @Query("select c from Comments c where c.customer.userId =:userId")
    Optional<List<Comments>> findAllByUserId(String userId);
}
