package project.shop.portfolio.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Cart;
import project.shop.portfolio.domain.Order;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.dto.CartDTO;
import project.shop.portfolio.dto.OrderHistoryDTO;
import project.shop.portfolio.dto.OrderItemDTO;
import project.shop.portfolio.dto.StoreItemDTO;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class StoreTests {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ReviewService reviewService;

    @Test
    public void getStoreItemDTOTest() throws Exception {

        StoreItemDTO storeItemDTO = itemService.getStoreItemDTOList(5L);
        System.out.println("storeItemDTOList = " + storeItemDTO);

    }

    @Test
    public void cartDTOList() throws Exception {
        //given
        List<CartDTO> cartDTOList = cartService.getCartDTOList("xun415");
        for (CartDTO cartDTO : cartDTOList) {
            System.out.println("cartDTO = " + cartDTO);
        }

        //when

        //then

    }

    @Test
    public void deleteFromCart() throws Exception {
        //given
        cartService.deleteCartByCartId("xun415",11L);
        List<Cart> xun415 = cartService.getCartList("xun415");
        for (Cart cart : xun415) {
            System.out.println("cart = " + cart);
        }

        //when

        //then

    }

    @Test
    public void orderHistory() throws Exception {
        //given
        List<OrderHistoryDTO> orderHistoryDTOListByUserId = orderService.getOrderHistoryDTOListByUserId(12L);
        for (OrderHistoryDTO orderHistoryDTO : orderHistoryDTOListByUserId) {
            System.out.println("orderHistoryDTO = " + orderHistoryDTO);
        }

    }

    @Test
    public void getReview() throws Exception {
        //given
        Optional<Review> review = reviewService.getReviewByUserIdAndItemId("xun415", 5L);
        Review review1 = review.get();
        review1.getText();
        System.out.println("review1.getText() = " + review1.getText());
        System.out.println("review1.getGrade() = " + review1.getGrade());
        System.out.println("review1 = " + review1);

    }

    @Test
    public void getOrderItemDTOList() throws Exception {
        //given
        List<OrderItemDTO> orderItemDTOList = orderService.getOrderItemDTOList(32L);
        for (OrderItemDTO orderItemDTO : orderItemDTOList) {
            System.out.println("orderItemDTO = " + orderItemDTO);
        }

    }

    @Test
    public void orderStatus() throws Exception {
        //given


        //when

        //then

    }

}
