package project.shop.portfolio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project.shop.portfolio.domain.*;
import project.shop.portfolio.dto.*;
import project.shop.portfolio.security.dto.CustomerAuthDTO;
import project.shop.portfolio.service.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    @Value("${project.shop.upload.path}")//application.yml의 변수.파일이 저장될 루트 파일경로
    private String uploadPath;
    private final ItemService itemService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final CustomerService customerService;
    private final CartService cartService;
    private final OrderService orderService;
    private final DeliveryService deliveryService;



    @PostMapping("/delivery/{id}/change")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    @ResponseBody
    public String changeDeliveryStatus(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,
                                       @PathVariable("id") Long id, Model model,@RequestParam("data")String data) throws ParseException {

        JSONParser jsonParser = new JSONParser();
        String status = (String) jsonParser.parse(data);

        deliveryService.modifyDeliveryStatus(id,status);

        return "변경완료";
    }
    //주문취소(결제완료 상태일때만)
    //'/store/order/'+orderId+'/delete'
    @PostMapping("/order/{orderId}/delete")
    public String cancelOrder(@PathVariable("orderId")Long orderId){
        log.info("storeController.cancelOrder. orderId : "+orderId);
        orderService.cancleOrder(orderId);

        return "redirect:/customer/orderHistory";
    }

    @GetMapping("/purchaseList")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String purchaseList(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,
                               Model model){
        String userId = customerAuthDTO.getUserId();
        Customer customer = customerService.findByUserid(userId);
        Long brandId = customer.getBrand().getId();

        List<PurchaseDTO> brandPurchaseList = orderService.getBrandPurchaseList(brandId);
        model.addAttribute("list",brandPurchaseList);

        return "seller/purchaseList";
    }
    //주문번호로 주문정보 보기(오더 아이템 리스트)
    //th:href="@{/store/order/{orderNum}(orderNum=${dto.orderNumber})}"
    @GetMapping("/order/{orderNum}")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String orderInfo(@PathVariable("orderNum")Long orderNumber,Model model){
        System.out.println("orderNumber = " + orderNumber);
        //상품명 색상 사이즈 수량
        List<OrderItemDTO> orderItemDTOList = orderService.getOrderItemDTOList(orderNumber);
        model.addAttribute("orderItemList",orderItemDTOList);

        return "seller/orderItemList";
    }

    @GetMapping("/main")
    public String storeMain(PageRequestDTO pageRequestDTO, Model model){
        //화면에서 page,size등 pageRequestDTO 파라미터들을 전달하면 자동 수집.
        //카테고리 리스트 & 디폴트 아이템리스트 페이징으로 넣어주기
        List<CategoryDTO> categoryList =  categoryService.getCategoryDTOList();
        //Category.name =부모카테고리네임. Category.childCategoryNames.get(i) = 자식 카테고리 이름들
        model.addAttribute("categoryList",categoryList);
        model.addAttribute("list",itemService.getList(pageRequestDTO));

        return "store/storeMain";
    }

    //스토어에서 아이템 클릭시
//    <a th:href="@{/store/item(id=${dto.id})} ">
    @GetMapping("/item")
    public String storeDetail(String id, Model model){
//        System.out.println("id = " + id);
        List<CategoryDTO> categoryList =  categoryService.getCategoryDTOList();
        //Category.name =부모카테고리네임. Category.childCategoryNames.get(i) = 자식 카테고리 이름들
        model.addAttribute("categoryList",categoryList);
        StoreItemDTO storeItemDTO = itemService.getStoreItemDTOList(Long.valueOf(id));
        try{
            List<Review> reviewList = itemService.getReviewListByItemId(Long.valueOf(id));
            model.addAttribute("reviewList",reviewList);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        model.addAttribute("dto",storeItemDTO);

        return "/store/storeDetail";
    }

    @PostMapping("/cart/{optionId}/{quantity}")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String addCart(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,
                            @PathVariable("optionId")String optionId,
                            @PathVariable("quantity")int quantity){

        String userId = customerAuthDTO.getUserId();

        cartService.addCart(userId,optionId,quantity);

        return "redirect:/store/cartList";
    }
    @GetMapping("/cartList")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String cartList(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,Model model){
        String userId = customerAuthDTO.getUserId();

        List<CartDTO> cartDTOList = cartService.getCartDTOList(userId);
        model.addAttribute("cartList",cartDTOList);


        return "customer/cart";
    }

    //카트에서 삭제
    @PostMapping("/cart/delete")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String deleteCart(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO
    ,@RequestParam String data) throws ParseException {
    //jsonArray로 "cart_Id": 카트아이디가 들어옴. cart_Id로 삭제
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(data);
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;
            String cart_id = (String) jsonObject.get("cart_Id");
            System.out.println("cart_id = " + cart_id);
            cartService.deleteCartByCartId(Long.valueOf(cart_id));
        }

        return "redirect:/store/main";
    }
    //카트에서 주문(orderForm)
    @PostMapping("order")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String orderForm(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,
            @RequestParam String data,Model model) throws ParseException {
        //JSONArray로 p_Id (itemDetail), c_Id(카트),p_Price
        //w_Quantity, pp_Name, p_Color,p_Size, pp_thumb,p_Brand parent_p_Id가 들어옴
        List<OrderFromDTO> orderFromDTOList = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParser.parse(data);
        for (Object o : jsonArray) {
            JSONObject jsonObject = (JSONObject) o;

            String p_size = (String) jsonObject.get("p_Size");
            String pp_name = (String) jsonObject.get("pp_Name");
            String w_quantity = (String) jsonObject.get("w_Quantity");
            String p_price = (String) jsonObject.get("p_Price");
            String pp_thumb = (String) jsonObject.get("pp_thumb");
            String itemDetail_id = (String) jsonObject.get("p_Id");//ItemDetail id
            String p_color = (String) jsonObject.get("p_Color");

            //cart에서 삭제한뒤, orderForm으로 보내줌
            String cart_id = (String) jsonObject.get("c_Id");//cart id
            cartService.deleteCartByCartId(customerAuthDTO.getUserId(),Long.valueOf(cart_id));

            OrderFromDTO orderFormDTO = cartService.getOrderFormDTO(itemDetail_id,p_size,pp_name,p_price,w_quantity,pp_thumb,p_color);
            orderFromDTOList.add(orderFormDTO);

        }
        model.addAttribute("orderFormList",orderFromDTOList);

        //배송지 정보: 이름 주소 연락처1 2
        String userId = customerAuthDTO.getUserId();
        Customer byUserid = customerService.findByUserid(userId);
        OrderDeliveryDTO orderDeliveryDTO = OrderDeliveryDTO.builder()
                .name(byUserid.getName())
                .phoneNum1(byUserid.getPhoneNumber())
                .phoneNum2(byUserid.getPhoneNumber2())
                .address(byUserid.getAddress())
                .build();
        orderDeliveryDTO.makeDTOPhoneNumberAndEmail();
        model.addAttribute("deliveryInfo",orderDeliveryDTO);
        return "/customer/orderForm";
    }

    @PostMapping("/order/new")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String order(@RequestParam("itemInfoList")String itemInfoJSON,@RequestParam("orderInfo")String orderInfoJSON,@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO ) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject orderInfo = (JSONObject) jsonParser.parse(orderInfoJSON);
        String customerName = (String) orderInfo.get("customerName");
        String zipcode = (String) orderInfo.get("zipcode");
        String city = (String) orderInfo.get("city");
        String street = (String) orderInfo.get("street");
        String phoneNumber1 = (String) orderInfo.get("phoneNumber1");
        String phoneNumber2 = (String) orderInfo.get("phoneNumber2");


        String userId = customerAuthDTO.getUserId();
        Customer byUserid = customerService.findByUserid(userId);
        Long customerId = byUserid.getId();


        Delivery delivery = Delivery.builder()
                .address(new Address(city,street,zipcode))
                .phoneNumber(phoneNumber1)
                .status(DeliveryStatus.결제완료)
                .build();


        JSONArray itemInfo = (JSONArray) jsonParser.parse(itemInfoJSON);
        for (Object o : itemInfo) {
            JSONObject jsonObject = (JSONObject) o;
            String detailID = (String) jsonObject.get("detailID");
            String price = (String) jsonObject.get("price");
            String quantity = (String) jsonObject.get("quantity");

            Long orderId = orderService.order(customerId, Long.valueOf(detailID), delivery, Integer.valueOf(quantity));

        }


        return "/customer/myPage";
    }




}
