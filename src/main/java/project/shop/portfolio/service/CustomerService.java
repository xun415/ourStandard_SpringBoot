package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Address;
import project.shop.portfolio.domain.Brand;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.Role;
import project.shop.portfolio.dto.CustomerDTO;
import project.shop.portfolio.dto.WaitingBrandManagerDTO;
import project.shop.portfolio.repository.BrandRepository;
import project.shop.portfolio.repository.CustomerRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)//조회성능 최적화. C U D시엔 readOnly 해제
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final BrandRepository brandRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(Customer customer){
        validateDuplicateMember(customer);
        String encodedPassword = passwordEncoder.encode(customer.getPassword());
        customer.changePassword(encodedPassword);
        customerRepository.save(customer);
        return customer.getId();
    }
    @Transactional
    public void deleteCustomer(Long id){
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()){
            Customer customer = result.get();
            customer.deleteCustomer(); //자식테이블들을 위해 실제 삭제가 아닌 개인정보들을 삭제
        }
    }
    @Transactional
    public void changePassword(Long id,String newPassword){
        //암호화 알고리즘 적용하기
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()) {
            Customer customer = result.get();
            customer.changePassword(passwordEncoder.encode(newPassword));
        }
    }
    //대기중인 기업회원 계정 목록
    public List<WaitingBrandManagerDTO> getWaitingBrandManagerList(){
        //대기 기업회원(비지니스넘버ㅇ, 역할엔 브랜드매니저 없는 회원)
        List<Object[]> waitingBrandManagerList = customerRepository.findWaitingBrandManagerList();
        List<WaitingBrandManagerDTO> dtoList = new ArrayList<>();

        for (Object[] objects : waitingBrandManagerList) {
              String strId = String.valueOf(objects[0]) ;
            Long id = Long.valueOf(strId);
            String birthday = (String)objects[8];
            String businessNumber = (String)objects[9];
            String email = (String)objects[10];
            String name = (String)objects[11];
            String phoneNumber1 = (String)objects[13];
            String phoneNumber2 = (String)objects[14];
            String userId = (String)objects[15];


            WaitingBrandManagerDTO waitingBrandManagerDTO= WaitingBrandManagerDTO.builder()
                    .id(id)
                    .userId(userId)
                    .name(name)
                    .birthday(birthday)
                    .phoneNumber1(phoneNumber1)
                    .phoneNumber2(phoneNumber2)
                    .email(email)
                    .businessNumber(businessNumber)
                    .build();
            Brand brandByBusinessNumber = brandRepository.findBrandByBusinessNumber(businessNumber);
            String brandName = brandByBusinessNumber.getName();
            waitingBrandManagerDTO.setBrandName(brandName);
            dtoList.add(waitingBrandManagerDTO);

        }

        return dtoList;
    }
    //기업회원 가입 승인 처리
    @Transactional
    public void approveBrandManager(Long id){
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()) {
            Customer customer = result.get();
            customer.getRoleSet().add(Role.BrandManager);
            Brand brand = brandRepository.findBrandByBusinessNumber(customer.getBusinessNumber());
            brand.getCustomerList().add(customer);
            customer.setBrand(brand);
        }
    }
    @Transactional
    public void rejectBrandManager(Long id){
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()) {
            Customer customer = result.get();
            customer.deleteBusinessNumber();
        }

    }
    //회원 탈퇴 처리
    @Transactional
    public void deleteActiveFalseCustomer(Long id){
        Optional<Customer> result = customerRepository.findById(id);
        if (result.isPresent()){
            Customer customer = result.get();
            customerRepository.delete(customer);
        }
    }


    @Transactional
    public void modify(Customer customer, String password, Address address, String phoneNum1, String phoneNum2){

        String encodedPW = passwordEncoder.encode(password);
        customer.modifyPasswordAddressAndPhoneNumber(encodedPW,address,phoneNum1,phoneNum2);

    }
    private void validateDuplicateMember(Customer customer) {
        try {

            List<Customer> findMembers = customerRepository.findByEmail(customer.getEmail());
            Optional<Customer> result = customerRepository.findByUserId(customer.getUserId());
            if (result.isPresent()) {
                throw new IllegalStateException("이미 존재하는 아이디 입니다.");
            }
            if (!findMembers.isEmpty()) {
                throw new IllegalStateException("이미 존재하는 회원입니다.");
                //실무에선 멀티스레드를 고려해서 DB 제약조건으로 한번 더 체크해주는 것이 좋음
            }
        }catch(NullPointerException e){
            e.printStackTrace();
        }
    }
    public List<Customer> findMembers(){
        return customerRepository.findAll();
    }
    public Customer findCustomerForAdmin(Long customerId){
        Optional<Customer> result = customerRepository.findById(customerId);
        if(result.isPresent()) return result.get();
        else return null;
    }
    public Customer findCustomerForUser(String userId,String password){
        Optional<Customer> result = customerRepository.findByUserId(userId);

        if (result.isPresent()){
            Customer customer = result.get();
            if(passwordEncoder.matches(password,customer.getPassword()) ==true){
                return customer;
            }else{
                throw new IllegalStateException("비밀번호가 틀렸습니다.");
            }
        }
        else throw new IllegalStateException("없는 아이디입니다.");

    }


    public Boolean findByCustomerName(String name) {
        Optional<Customer> byUserId = customerRepository.findByUserId(name);
        if (byUserId.isPresent()){
            return true;
        }
        else return false;
    }
    public Customer dtoToEntity(CustomerDTO dto){
        if (dto.getBrandName()==null){
            //일반 회원일 경우
            Customer customer =Customer.builder()
                    .userId(dto.getUserId())
                    .password(dto.getPassword())
                    .address(new Address(dto.getCity(),dto.getStreet(),dto.getZipcode()))
                    .name(dto.getName())
                    .birthDay(dto.getBirthday())
                    .phoneNumber(dto.getPhoneNum1())
                    .phoneNumber2(dto.getPhoneNum2())
                    .email(dto.getEmail())
                    .build();
            customer.addCustomerRole(Role.Customer);
            return customer;
        }
        else{
            //브랜드회원은 가입시엔 브랜드이름만 표기하고, Role은 나중에 줌.
            Customer brandCustomer =Customer.builder()
                    .userId(dto.getUserId())
                    .password(dto.getPassword())
                    .name(dto.getName())
                    .birthDay(dto.getBirthday())
                    .address(new Address(dto.getCity(),dto.getStreet(),dto.getZipcode()))
                    .phoneNumber(dto.getPhoneNum1())
                    .phoneNumber2(dto.getPhoneNum2())
                    .email(dto.getEmail())
                    .businessNumber(dto.getBusinessNumber())
                    .build();
            brandCustomer.addCustomerRole(Role.Customer);
            return brandCustomer;
            
        }


    }

    public Customer findByUserid(String userId) {
        Optional<Customer> result = customerRepository.findByUserId(userId);
            return result.get();
    }

    public CustomerDTO EntityToDto(Customer customer) {
        CustomerDTO customerDTO = CustomerDTO.builder()
                                    .userId(customer.getUserId())
                                    .name(customer.getName())
                                    .city(customer.getAddress().getCity())
                                    .street(customer.getAddress().getStreet())
                                    .zipcode(customer.getAddress().getZipcode())
                                    .email(customer.getEmail())
                                    .phoneNum1(customer.getPhoneNumber())
                                    .build();
        if (customer.getPhoneNumber2()!=null){
            customerDTO.setPhoneNum2(customer.getPhoneNumber2());
        }
        System.out.println("customerDTO = " + customerDTO);
        customerDTO.makeDTOPhoneNumberAndEmail();
        return customerDTO;

    }
    @Transactional
    public void deleteCustomerByUserId(String userId) {
        customerRepository.deleteByUserId(userId);
    }

    public Optional<Customer> findByNameAndEmail(String name, String email) {
        return customerRepository.findByNameAndEmail(name,email);
    }

    public Optional<Customer> findByUserIdAndNameAndEmail(String id, String name, String email) {
        return customerRepository.findByUserIdAndNameAndEmail(id,name,email);
    }
}
