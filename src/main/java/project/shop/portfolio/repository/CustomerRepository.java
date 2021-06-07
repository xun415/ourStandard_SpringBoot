package project.shop.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CustomerRepository extends JpaRepository<Customer,Long> {

    List<Customer> findByEmail(String email);
    @Query("select c from Customer c where c.businessNumber is not null and c.roleSet <> ?1")
    List<Customer> findByBusinessNumberIsNotNullAndRoleSetIsNot(Set<Role> role2);

//    @Query(value = "SELECT * from Customer c LEFT join customer_role_set rs " +
//            "on c.customer_id = rs.customer_customer_id where rs.role_set != 1 AND c.business_number IS NOT null"
//            ,nativeQuery = true)
    @Query(value = "SELECT * FROM customer c JOIN role_view v ON c.customer_id = v.id " +
            "WHERE v.COUNT =1 AND c.business_number IS NOT NULL;",nativeQuery = true)
    List<Object[]> findWaitingBrandManagerList();

    Optional<Customer> findByUserId(String userId);

    void deleteByUserId(String userId);

    Optional<Customer> findByNameAndEmail(String name, String email);

    Optional<Customer> findByUserIdAndNameAndEmail(String id, String name, String email);
}
