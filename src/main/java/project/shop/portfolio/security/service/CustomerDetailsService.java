package project.shop.portfolio.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.repository.CustomerRepository;
import project.shop.portfolio.security.dto.CustomerAuthDTO;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
@Transactional
public class CustomerDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("CustomerUserDetailsService loadUserByUsername : "+username);

        Optional<Customer> result = customerRepository.findByUserId(username);

        if (result.isEmpty()){
            throw new UsernameNotFoundException("없는 아이디 입니다.");
        }
        Customer customer = result.get();

        log.info("--------------------------------------");
        //log.info(customer);

        CustomerAuthDTO customerAuthDTO = new CustomerAuthDTO(
                customer.getUserId(),
                customer.getPassword(),
                customer.getRoleSet().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name())).collect(Collectors.toSet())
        );
        customerAuthDTO.setName(customer.getName());
        log.info("CustomerDetailsService.loadUserByUsername. customerAuthDTO 작성");
        log.info("customerAuthDTO password : "+customerAuthDTO.getPassword());
        log.info("isEnabled : "+customerAuthDTO.isEnabled());
        log.info("AccountNonExpired : "+customerAuthDTO.isAccountNonExpired());


        return customerAuthDTO;
    }
}
