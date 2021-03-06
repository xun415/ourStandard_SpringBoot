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

    //????????????
    @GetMapping("/register")
    public String registerForm(Model model){
        log.info("CustomerController.registerForm");

        model.addAttribute("customerForm",new CustomerDTO());

        return "/customer/register";
    }

    @PostMapping("/register")
    public String register(CustomerDTO customerRegisterDTO, BindingResult result){
        //BindingResult result??? ??????????????? ????????????, ????????? ????????? ?????? ?????? ????????? ???????????? ?????? ??????
        if(result.hasErrors()){
            return "/customer/register";
        }
        customerRegisterDTO.makePhoneNumberAndEmail();
        //??????????????? dtoToEntity?????? bussinessNumber??? ????????? ????????????, ????????? customer???.
        Customer customer = customerService.dtoToEntity(customerRegisterDTO);
        customerService.join(customer);

        //brand??????????????? ????????? ????????????
        //??????????????? ?????? ??????.(????????? ??????????????? ??????, ????????? ??????)

        if(customerRegisterDTO.getBrandName()!=null&&customerRegisterDTO.getBrandName().length()>1){

            brandService.registerBrand(customerRegisterDTO.getBrandName(),customerRegisterDTO.getBusinessNumber());
        }

        return "redirect:/login";
    }

    //??????????????????
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

    //???????????????
    @GetMapping("/myPage")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String myPageMain(){

        return "customer/myPage";
    }

    //????????????
    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String delete(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO){
        //?????? ????????? ?????? ?????? ??????
        log.info("customerController.delete");
        log.info("customerAuthDTO = "+customerAuthDTO);
        String userId = customerAuthDTO.getUserId();
        customerService.deleteCustomerByUserId(userId);
        return "redirect:/index";
    }

    //????????? ??????
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
            return "???????????? ???????????? ???????????? ??????????????????.";
        }else return "???????????? ?????? ???????????????.";


    }

    //???????????? ??????
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
            return "????????? ???????????? ?????? ??????????????? ??????????????????.";

        }else{
            return "???????????? ????????? ????????? ????????? ????????????.";
        }

        
    }
    //????????????.?????????????????????
    @PostMapping("/check_Id")
    @ResponseBody
    public String checkId(@RequestParam String name, HttpServletRequest request, HttpServletResponse resp){
        Boolean result = customerService.findByCustomerName(name);//?????? ????????? true, ????????? false

        if (result ==true){
            return "<strong style='color :red'>?????? ???????????? ??????????????????.</strong>";
        }
        else return "<strong style='color :green'>????????? ??? ?????? ????????? ?????????.</strong>";
    }
}
