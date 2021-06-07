package project.shop.portfolio.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
public class MainController {
    @GetMapping("/login")
    public String loginForm(){
        System.out.println("MainController.loginForm");

        return "customer/loginForm";
    }
}
