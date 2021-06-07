package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Comments;
import project.shop.portfolio.domain.Customer;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.dto.BrandReviewDTO;
import project.shop.portfolio.dto.CommentsDTO;
import project.shop.portfolio.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    public List<Review> getReviewListByItemId(Long itemId){
        Optional<List<Review>> result = reviewRepository.findAllByItemId(itemId);
        if (result.isPresent()){
            return result.get();
        }
        else return null;
    }
    public List<Review> getReviewListByBrandId(Long brandId){
        Optional<List<Review>> result = reviewRepository.findAllByBrandId(brandId);
        if (result.isPresent()){
            return result.get();
        }
        else return null;
    }
    public Long insertReview(Review review){
        reviewRepository.save(review);
        return review.getId();
    }
    public List<Review> getReviewListByUserId(String userId){
        Optional<List<Review>> result = reviewRepository.findAllByUserId(Long.valueOf(userId));
        if (result.isPresent()){
            return result.get();
        }
        else return null;
    }
    public void modifyReview(Long reviewId,int grade,String text){
        Optional<Review> result = reviewRepository.findById(reviewId);
        if (result.isPresent()){
            Review review = result.get();
            review.changeGrade(grade);
            review.changeText(text);
        }
    }
    public void deleteReview(Long reviewId){
        Optional<Review> result = reviewRepository.findById(reviewId);
        if (result.isPresent()){
            Review review = result.get();
            review.deleteReview();
        }
    }

    public Optional<Review> getReviewByUserIdAndItemId(String userId, Long itemId) {
        return reviewRepository.findByUserIdAndItemId(userId,itemId);
    }

    public Review getReview(Long reviewId) {
        Optional<Review> byId = reviewRepository.findById(reviewId);
        return byId.get();
    }

    public List<BrandReviewDTO> getBrandReviewDTOListByBrandId(Long brandId) {

        try{
            List<BrandReviewDTO> brandReviewDTOList = new ArrayList<>();

            List<Review> reviewListByBrandId = getReviewListByBrandId(brandId);
            for (Review review : reviewListByBrandId) {

                BrandReviewDTO brandReviewDTO = BrandReviewDTO.builder()
                        .reviewId(review.getId())
                        .text(review.getText())
                        .grade(review.getGrade())
                        .itemId(review.getItem().getId())
                        .itemName(review.getItem().getName())
                        .writer(review.getCustomer().getUserId())
                        .build();
//                if(review.getCommentsList()!=null){
//                    List<CommentsDTO> commentsDTOList = brandReviewDTO.getCommentsDTOList();
//                    List<Comments> commentsList = review.getCommentsList();
//                    for (Comments comments : commentsList) {
//                        CommentsDTO commentsDTO = CommentsDTO.builder()
//                                .commentId(comments.getId())
//                                .reviewId(review.getId())
//                                .itemName(review.getItem().getName())
//                                .text(comments.getText())
//                                .writer(comments.getCustomer().getName())
//                                .build();
//
//                        commentsDTOList.add(commentsDTO);
//                    }
//                }
                brandReviewDTOList.add(brandReviewDTO);
            }
            return brandReviewDTOList;
        }catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }
}
