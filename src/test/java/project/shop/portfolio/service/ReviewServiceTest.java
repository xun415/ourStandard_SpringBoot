package project.shop.portfolio.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.dto.BrandReviewDTO;
import project.shop.portfolio.dto.CommentsDTO;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class ReviewServiceTest {
    @Autowired
    private ItemService itemService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ReviewService reviewService;

    @Test
    public void getBrandReviewDTOList() throws Exception {
        List<BrandReviewDTO> brandReviewDTOListByBrandId = reviewService.getBrandReviewDTOListByBrandId(4L);
        for (BrandReviewDTO brandReviewDTO : brandReviewDTOListByBrandId) {
            System.out.println("brandReviewDTO = " + brandReviewDTO);

        }

    }
}
