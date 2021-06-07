package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Brand;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Long> {
    Optional<Brand> findByBusinessNumber(String businessNumber);

    Brand findBrandByBusinessNumber(String businessNumber);

    String findBrandNameByBusinessNumber(String businessNumber);
}
