//package project.shop.portfolio.controller;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import project.shop.portfolio.security.dto.CustomerAuthDTO;
//
//@Controller
//@Log4j2
//@RequestMapping("/sample")
//public class SampleController {
////    @GetMapping("/member")
////    public String exMember(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO){
////        log.info("exMember....");
////
////        log.info("--------------------");
////
////        log.info(customerAuthDTO);
////        return "sample/member";
////    }
//
////    @PreAuthorize("hasRole('ROLE_BrandManager')")
////    @GetMapping("/test")
////    public String header(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO){
////        log.info("test....");
////
////        log.info("--------------------");
////
////        log.info(customerAuthDTO);
////        return "header";
////
////    }
////    @GetMapping("/index")
////    public String index(){
////        log.info("controller index");
////        return "index";
////    }
////    @GetMapping("/wishList")
////    public String wishList(){
////        log.info("controller index");
////        return "cart/wishList";
////    }
////    @GetMapping("/orderForm")
////    public String orderForm(){
////        log.info("controller index");
////        return "cart/orderForm";
////    }
//
//}
