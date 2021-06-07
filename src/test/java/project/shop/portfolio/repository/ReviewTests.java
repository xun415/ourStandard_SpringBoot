//package project.shop.portfolio.repository;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import project.shop.portfolio.domain.*;
//import project.shop.portfolio.domain.item.Item;
//import project.shop.portfolio.domain.item.ItemDetail;
//
//import java.util.List;
//import java.util.Optional;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Transactional
//public class ReviewTests {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private BrandRepository brandRepository;
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private ItemDetailRepository itemDetailRepository;
//    @Autowired
//    private DeliveryRepository deliveryRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//    @Autowired
//    private CustomerRepository customerRepository;
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//    @Autowired
//    private ReviewRepository reviewRepository;
//    @Autowired
//    private CommentsRepository commentsRepository;
//
//
//    @Test
//    public void insertReview() throws Exception {
//
//        //아이템 등록
//        Optional<Item> byId2 = itemRepository.findById(2L);
//        if (byId2.isPresent()) {
//            Item item = byId2.get();
//
//            Optional<Customer> byId = customerRepository.findById(11L);
//            Optional<Customer> byId1 = customerRepository.findById(18L);
//            if (byId.isPresent()) {
//                if (byId1.isPresent()) {
//                    Customer customer = byId.get();
//                    Customer customer2 = byId1.get();
//
//                    Review review = Review.builder()
//                            .customer(customer)
//                            .grade(4)
//                            .text("그냥그랬어요")
//                            .item(item)
//                            .build();
//                    Review savedReview = reviewRepository.save(review);
//                    Long savedReviewId = savedReview.getId();
//
//
//                    Comments comments = Comments.builder()
//                            .review(review)
//                            .text("사이즈는 어떤가요")
//                            .customer(customer2)
//                            .build();
//                    Comments comments2 = Comments.builder()
//                            .review(review)
//                            .text("적당해요")
//                            .customer(customer)
//                            .build();
//
//                    Optional<Review> byId3 = reviewRepository.findById(savedReviewId);
//                    if (byId3.isPresent()){
//                        Review review1 = byId3.get();
//                        List<Comments> commentsList = review1.getCommentsList();
//                        commentsList.add(comments);
//                        commentsList.add(comments2);
//                    }
//                    commentsRepository.save(comments);
//                    commentsRepository.save(comments2);
//
//
//
//                    Optional<List<Review>> reviewRepositoryAllByItem = reviewRepository.findAllByItem(item);
//
//
//                    if (reviewRepositoryAllByItem.isPresent()) {
//                        List<Review> reviews = reviewRepositoryAllByItem.get();
//                        for (Review review1 : reviews) {
//                            System.out.println("review1.getText() = " + review1.getText());
//                            System.out.println("review1.getGrade() = " + review1.getGrade());
//                            System.out.println("review1.getCustomer().getName() = " + review1.getCustomer().getName());
//                            System.out.println("review1.getItem().getName() = " + review1.getItem().getName());
//
//                            List<Comments> commentsList = review1.getCommentsList();
//
//
//                            System.out.println("commentsList = " + commentsList);
//                            for (Comments comments1 : commentsList) {
//                                System.out.println("comments1.getText() = " + comments1.getText());
//                                System.out.println("comments1.getCustomer().getName() = " + comments1.getCustomer().getName());
//                            }
//
//
//
//                        }
//                    }
//                }
//            }
//        }
//
//
//
//
//    }
//}
