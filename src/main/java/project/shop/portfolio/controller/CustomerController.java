package project.shop.portfolio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import project.shop.portfolio.common.Email;
import project.shop.portfolio.common.TempPWD;
import project.shop.portfolio.domain.Address;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.Order;
import project.shop.portfolio.dto.CustomerDTO;
import project.shop.portfolio.dto.OrderHistoryDTO;
import project.shop.portfolio.security.dto.CustomerAuthDTO;
import project.shop.portfolio.service.BrandService;
import project.shop.portfolio.service.CustomerService;
import project.shop.portfolio.service.OrderService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final BrandService brandService;
    private final OrderService orderService;

    @GetMapping("/index")
    public String index(){
        log.info("controller index");
        return "index";
    }


    @GetMapping("/orderHistory")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String orderHistory(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO, Model model){
        Customer customer = customerService.findByUserid(customerAuthDTO.getUserId());
        Long customerId = customer.getId();
        try{

        List<OrderHistoryDTO> orderHistoryDTOList = orderService.getOrderHistoryDTOListByUserId(customerId);

        model.addAttribute("orderHistoryList",orderHistoryDTOList);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        return "/customer/orderHistory";
    }

    //회원등록
    @GetMapping("/register")
    public String registerForm(Model model){
        log.info("CustomerController.registerForm");

        model.addAttribute("customerForm",new CustomerDTO());

        return "/customer/register";
    }

    @PostMapping("/register")
    public String register(CustomerDTO customerRegisterDTO, BindingResult result){
        //BindingResult result를 매개변수로 추가하면, 오류시 튕기지 않고 오류 정보를 포함해서 코드 실행
        if(result.hasErrors()){
            return "/customer/register";
        }
        customerRegisterDTO.makePhoneNumberAndEmail();
        //기업회원은 dtoToEntity에서 bussinessNumber에 정보만 입력하고, 역할은 customer만.
        Customer customer = customerService.dtoToEntity(customerRegisterDTO);
        customerService.join(customer);

        //brand서비스에서 브랜드 가입하기
        //커스터머는 추가 안함.(승인시 브랜드에도 넣고, 룰에도 추가)

        if(customerRegisterDTO.getBrandName()!=null&&customerRegisterDTO.getBrandName().length()>1){

            brandService.registerBrand(customerRegisterDTO.getBrandName(),customerRegisterDTO.getBusinessNumber());
        }

        return "redirect:/login";
    }

    //회원정보수정
    @GetMapping("/modify")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String modifyForm(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,Model model){
        log.info("Controller.modifyForm");
        log.info(customerAuthDTO.getUserId());
        String userId = customerAuthDTO.getUserId();
        Customer customer = customerService.findByUserid(userId);
        log.info(customer);
        CustomerDTO customerDTO = customerService.EntityToDto(customer);
        log.info(customerDTO);
        model.addAttribute("form",customerDTO);

        return "/customer/modify";
    }

    @PostMapping("/modify")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String modify(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,CustomerDTO customerDTO){
        System.out.println("CustomerController.PostModify.customerDTO = " + customerDTO);
        customerDTO.makePhoneNumberAndEmail();

        String userId = customerAuthDTO.getUserId();
        Customer originalCustomer = customerService.findByUserid(userId);

//        originalCustomer.modifyPasswordAddressAndPhoneNumber(customerDTO.getPassword(),new Address(customerDTO.getCity(), customerDTO.getCity(), customerDTO.getZipcode()), customerDTO.getPhoneNum1(),customerDTO.getPhoneNum2() );
        customerService.modify(originalCustomer,customerDTO.getPassword(),new Address(customerDTO.getCity(), customerDTO.getStreet(), customerDTO.getZipcode()), customerDTO.getPhoneNum1(),customerDTO.getPhoneNum2() );
        System.out.println("CustomerController.PostModify.originalCustomer.AfterChange = " + originalCustomer);
        return "redirect:/customer/modify";
    }

    //마이페이지
    @GetMapping("/myPage")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String myPageMain(){

        return "customer/myPage";
    }

    //회원탈퇴
    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String delete(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO){
        //완전 탈퇴가 아닌 정보 삭제
        log.info("customerController.delete");
        log.info("customerAuthDTO = "+customerAuthDTO);
        String userId = customerAuthDTO.getUserId();
        customerService.deleteCustomerByUserId(userId);
        return "redirect:/index";
    }

    //아이디 찾기
    @GetMapping("findId")
    public String findIdForm(){

        return "customer/findId";
    }
    @PostMapping("findId")
    @ResponseBody
    public String findId(@RequestParam String data) throws ParseException, MessagingException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject =(JSONObject) jsonParser.parse(data);

        String name = (String)jsonObject.get("name");
        String email = (String)jsonObject.get("email");

        Optional<Customer> result = customerService.findByNameAndEmail(name, email);

        if (result.isPresent()){
            Customer customer = result.get();
            Email.sendFindIdResult(customer.getUserId(), customer.getEmail());
            return "요청하신 아이디를 이메일로 발송했습니다.";
        }else return "등록되지 않은 정보입니다.";


    }

    //비밀번호 찾기
    @GetMapping("/findPassword")
    public String findPasswordForm(){

        return "customer/findPassword";
    }

    @PostMapping("/findPassword")
    @ResponseBody
    public String findPassword(@RequestParam String data) throws ParseException, MessagingException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject =(JSONObject) jsonParser.parse(data);

        String id = (String)jsonObject.get("id");
        String name = (String)jsonObject.get("name");
        String email = (String)jsonObject.get("email");
        log.info("findPassword.data = "+data);

        Optional<Customer> result = customerService.findByUserIdAndNameAndEmail(id,name,email);
        if (result.isPresent()){
            Customer customer = result.get();
            String newPassword = TempPWD.randomPw();

            customerService.changePassword(customer.getId(), newPassword);

            Email.sendTempPWD(customer.getUserId(), customer.getEmail(), newPassword);
            return "등록된 이메일로 임시 비밀번호를 발송했습니다.";

        }else{
            return "입력하신 정보로 가입된 계정이 없습니다.";
        }

        
    }
    //회원가입.아이디중복검사
    @PostMapping("/check_Id")
    @ResponseBody
    public String checkId(@RequestParam String name, HttpServletRequest request, HttpServletResponse resp){
        Boolean result = customerService.findByCustomerName(name);//값이 있으면 true, 없으면 false

        if (result ==true){
            return "<strong style='color :red'>이미 사용중인 아이디입니다.</strong>";
        }
        else return "<strong style='color :green'>사용할 수 있는 아이디 입니다.</strong>";
    }
}
