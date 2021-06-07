package project.shop.portfolio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.dto.WaitingBrandManagerDTO;
import project.shop.portfolio.service.BrandService;
import project.shop.portfolio.service.CustomerService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_Admin')")
public class AdminController {
    private final CustomerService customerService;
    private final BrandService brandService;

    @GetMapping("/main")
    public String adminMain(Model model){
        log.info("AdminController.adminMain");
        try {
//            List<WaitingBrandManagerDTO> list = customerService.getWaitingBrandManagerList();
            List<WaitingBrandManagerDTO> list = customerService.getWaitingBrandManagerList();
            model.addAttribute("list",list);

        }catch(NullPointerException e){
            e.printStackTrace();
        }

        return "/admin/adminMain";
    }
    @ResponseBody
    @PostMapping("/approveBrandManager")
    public void approveBrandManager(@RequestParam String data) throws ParseException {
        log.info("ajax로 브랜드 매니저 승인 요청");
        log.info(data);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray =(JSONArray) parser.parse(data);
        for(int i=0;i<jsonArray.size();i++){
            jsonObject= (JSONObject)jsonArray.get(i);
            String c_id = (String)jsonObject.get("c_id");
            customerService.approveBrandManager(Long.valueOf(c_id));
        }

    }

    @RequestMapping("/deleteWaitingCustomer")
    public void rejectBrandManager(@RequestParam String data) throws ParseException {
        log.info("ajax로 브랜드 매니저 요청 삭제 요청");
        log.info(data);
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        jsonArray =(JSONArray) parser.parse(data);
        for(int i=0;i<jsonArray.size();i++){
            jsonObject= (JSONObject)jsonArray.get(i);
            String c_id = (String)jsonObject.get("c_id");
            customerService.rejectBrandManager(Long.valueOf(c_id));
        }

    }
}
