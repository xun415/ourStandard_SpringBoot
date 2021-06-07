package project.shop.portfolio.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import project.shop.portfolio.domain.Brand;
import project.shop.portfolio.domain.Comments;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.domain.item.Item;
import project.shop.portfolio.dto.BrandReviewDTO;
import project.shop.portfolio.dto.CommentsDTO;
import project.shop.portfolio.dto.ReviewDTO;
import project.shop.portfolio.security.dto.CustomerAuthDTO;
import project.shop.portfolio.service.BrandService;
import project.shop.portfolio.service.CustomerService;
import project.shop.portfolio.service.ItemService;
import project.shop.portfolio.service.ReviewService;

import java.util.List;
import java.util.Optional;

@Controller
@Log4j2
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ItemService itemService;
    private final ReviewService reviewService;
    private final CustomerService customerService;
    private final BrandService brandService;

    @GetMapping("/brand")
    @PreAuthorize("hasRole('ROLE_BrandManager')")
    public String brandReviewList(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,Model model){
        String userId = customerAuthDTO.getUserId();
        Customer byUserid = customerService.findByUserid(userId);
        Brand brand = byUserid.getBrand();
        Long brandId = brand.getId();
        try{
        List<BrandReviewDTO> brandReviewDTOList = reviewService.getBrandReviewDTOListByBrandId(brandId);
        model.addAttribute("reviewDTOList",brandReviewDTOList);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        return "seller/review";
    }

    @PostMapping("/new")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String addReview(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO, ReviewDTO reviewDTO){
        Long itemId = reviewDTO.getItemId();
        Item item = itemService.getItemById(itemId);
        String userId = customerAuthDTO.getUserId();
        Customer customer = customerService.findByUserid(userId);
        Review review =Review.builder()
                .item(item)
                .text(reviewDTO.getText())
                .grade(reviewDTO.getGrade())
                .customer(customer)
                .build();
        reviewService.insertReview(review);

        return "redirect:/review/"+reviewDTO.getItemId()+"/review";
    }
    @PostMapping("modify")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String modifyReview(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,ReviewDTO reviewDTO){

        reviewService.modifyReview(reviewDTO.getReviewId(), reviewDTO.getGrade(), reviewDTO.getText());

        return "redirect:/review/"+reviewDTO.getItemId()+"/review";
    }
    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String deleteReview(@AuthenticationPrincipal CustomerAuthDTO customerAuthDTO,ReviewDTO reviewDTO){
        reviewService.deleteReview(reviewDTO.getReviewId());

        return "redirect:/customer/orderHistory";
    }

    //@{item/{itemId}/review(itemId=${dto.itemId})}
    @GetMapping("{itemId}/review")
    @PreAuthorize("hasRole('ROLE_Customer')")
    public String getReview(@PathVariable("itemId")Long itemId, @AuthenticationPrincipal CustomerAuthDTO customerAuthDTO, Model model){
        String userId = customerAuthDTO.getUserId();
        Optional<Review> result = reviewService.getReviewByUserIdAndItemId(userId,itemId);
        System.out.println("itemId = " + itemId);
        if (result.isPresent()){
            Review review = result.get();
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .reviewId(review.getId())
                    .itemId(itemId)
                    .text(review.getText())
                    .grade(review.getGrade())
                    .build();
            model.addAttribute("review",reviewDTO);

        }else{
            ReviewDTO reviewDTO = ReviewDTO.builder()
                    .itemId(itemId)
                    .build();
            model.addAttribute("review",reviewDTO);
        }

        return "customer/review";
    }
}
