package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Comments;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.repository.CommentsRepository;
import project.shop.portfolio.repository.ReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final ReviewRepository reviewRepository;

    public Long insertComment(Long reviewId,Comments comments){
        Optional<Review> result = reviewRepository.findById(reviewId);
        if (result.isPresent()){
            Review review = result.get();
            review.getCommentsList().add(comments);
            Comments save = commentsRepository.save(comments);
            return save.getId();
        }
        else return null;

    }
    public void modifyComment(Long id,String text){
        Optional<Comments> result = commentsRepository.findById(id);
        if (result.isPresent()){
            Comments comments = result.get();
            comments.changeText(text);
        }
    }
    public void deleteComment(Long id){
            commentsRepository.deleteById(id);
    }

    public List<Comments> getCommentsByUserId(String userId){
        Optional<List<Comments>> result = commentsRepository.findAllByUserId(userId);
        if (result.isPresent()){
            return result.get();
        }
        else return null;
    }

}
