package project.shop.portfolio.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Order;
import project.shop.portfolio.dto.OrderHistoryDTO;
import project.shop.portfolio.dto.PurchaseDTO;

import java.util.List;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTests {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;


//    getOrderHistoryDTOListByUserId
    @Test
    public void getOrderHistoryDTOListTests() throws Exception {
    //given
        List<OrderHistoryDTO> orderHistoryDTOListByUserId = orderService.getOrderHistoryDTOListByUserId(12L);
        for (OrderHistoryDTO orderHistoryDTO : orderHistoryDTOListByUserId) {
            System.out.println("orderHistoryDTO = " + orderHistoryDTO);
        }
        //when

    //then

    }

    @Test
    public void getOrderIdByBrandId() throws Exception {
        //given
        List<PurchaseDTO> brandPurchaseList = orderService.getBrandPurchaseList(4L);
        for (PurchaseDTO purchaseDTO : brandPurchaseList) {
            System.out.println("purchaseDTO = " + purchaseDTO);
        }

        //when

        //then

    }
}
