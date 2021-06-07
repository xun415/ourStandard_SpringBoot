//package project.shop.portfolio.service;
//
//import org.assertj.core.api.Assertions;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Bean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import project.shop.portfolio.domain.Address;
//import project.shop.portfolio.domain.Customer;
//import project.shop.portfolio.domain.Role;
//import project.shop.portfolio.repository.CustomerRepository;
//
//import java.util.List;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Transactional
//public class CustomerServiceTests {
//    @Autowired
//    private CustomerService customerService;
//    @Autowired
//    private CustomerRepository customerRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
////    @Test
////    public void 암호화테스트() throws Exception {
////        String pw = "1111";
////        String encode = passwordEncoder.encode(pw);
////        System.out.println("encode = " + encode);
////        String pw2 ="1111";
////        String encode1 = passwordEncoder.encode(pw2);
////        System.out.println("encode1 = " + encode1);
////        System.out.println("passwordEncoder.matches(encode,pw) = " + passwordEncoder.matches(encode,pw));
////
////    }
////
////    @Test
////    @Transactional
////    public void 회원가입_회원가입아이디중복() throws Exception {
////        Customer customer2 = Customer.builder()
////                .name("태훈")
////                .address(new Address("city", "street", "12345"))
////                .email("xun415@naver.com")
////                .userId("xun415")
////                .phoneNumber("000-0000-0000")
////                .birthDay("1993-04-15")
////                .password("2222")
////                //.roleSet.add(Role.Customer)
////                .brand(null)
////                .build();
////
////
////        System.out.println("customer name = " + customer2.getName());
////        System.out.println("test.customer = " + customer2.toString());
////        Long joinedId = customerService.join(customer2);
////        Customer findCustomer = customerService.findCustomerForAdmin(joinedId);
////
////        System.out.println("findCustomer = " + findCustomer);
////        Assertions.assertThat(customer2).isEqualTo(findCustomer);
////
////    }
////
////    @Test
////    public void 로그인테스트() throws Exception {
////        String userId = "xun415";
////        String pw ="2222";
////        Customer customerForUser = customerService.findCustomerForUser(userId, pw);
////        System.out.println("customerForUser = " + customerForUser);
////    }
////
////    @Test
////    public void 비밀번호변경테스트() throws Exception {
////        //given
////        String userId = "xun415";
////        String pw ="2222";
////        Customer customer = customerService.findCustomerForUser(userId, pw);
////        Long id = customer.getId();
////        //when
////        String newPw = "1111";
////        customerService.changePassword(id,newPw);
////
////        //then
////        Customer pwChangedCustomer = customerService.findCustomerForUser(userId, newPw);
////
////    }
//
//    @Test
//    public void getList() throws Exception {
//        List<Object[]> waitingBrandManagerList = customerRepository.findWaitingBrandManagerList();
//        for (Object[] objects : waitingBrandManagerList) {
//            for (Object object : objects) {
//                System.out.println("object = " + object);
//            }
//        }
//
//    }
//}
