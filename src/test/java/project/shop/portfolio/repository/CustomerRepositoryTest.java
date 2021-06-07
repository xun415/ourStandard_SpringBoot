//package project.shop.portfolio.repository;
//
//import org.assertj.core.api.Assertions;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import project.shop.portfolio.domain.Address;
//import project.shop.portfolio.domain.Brand;
//import project.shop.portfolio.domain.Customer;
//import project.shop.portfolio.domain.Role;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.IntStream;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Transactional
//public class CustomerRepositoryTest {
//    @Autowired
//    private CustomerRepository customerRepository;
//    @Autowired
//    private BrandRepository brandRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Test
//    public void insertCustomerTest() throws Exception {
//        IntStream.rangeClosed(1,20).forEach(i->{
//            Customer customer = Customer.builder()
//                    .name("customer.."+i)
//                    .address(new Address("city","street"+i,"zipcode"+i))
////                    .active(true)
//                    .brand(null)
//                    .email("customer"+i+"@naver.com")
//                    .phoneNumber("010-4444-4444")
//                    .build();
//            customerRepository.save(customer);
//        });
//    }
//
//    @Test
//    @Commit
//    public void 회원가입_인증적용() throws Exception {
//        //given
//        Customer build = Customer.builder()
//                .name("박태훈")
//                .birthDay("1993-04-15")
//                .password(passwordEncoder.encode("1111"))
//                .userId("xun")
//                .email("xun415@naver.com")
//                .phoneNumber("000-0000-0000")
//                .address(new Address("city", "street", "zipcode"))
//                .brand(null)
//                .build();
//        build.addCustomerRole(Role.Customer);
//        customerRepository.save(build);
//
//
//
//        //when
//
//        //then
//
//    }
//
//    @Test
//    public void insertBrandWaitingCustomer() throws Exception {
//        IntStream.rangeClosed(1,2).forEach(i->{
//            Brand brand = Brand.builder()
//                    .name("Brand"+i)
//                    .build();
//            brandRepository.save(brand);
//
//            Customer customer = Customer.builder()
//                    .name("customer.."+i)
//                    .address(new Address("city","street"+i,"zipcode"+i))
////                    .active(false)
//                    .brand(brandRepository.getOne(brand.getId()))
//                    .email("customer"+i+"@naver.com")
//                    .phoneNumber("010-4444-4444")
//                    .build();
//            customerRepository.save(customer);
//        });
//        //given
//
//        //when
//        List<Customer> all = customerRepository.findAll();
//        for (Customer customer : all) {
//  //          Assertions.assertThat(customer.isActive()).isFalse();
//  //          System.out.println("customer.isActive() = " + customer.isActive());
//        }
//
//    }
//}
