//package project.shop.portfolio.repository;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Commit;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import project.shop.portfolio.domain.*;
//import project.shop.portfolio.domain.item.Item;
//import project.shop.portfolio.domain.item.ItemDetail;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.IntStream;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@Transactional
//public class OrderRepositoryTest {
//    @Autowired
//    private CustomerRepository customerRepository;
//    @Autowired
//    private OrderRepository orderRepository;
//    @Autowired
//    private ItemRepository itemRepository;
//    @Autowired
//    private ItemDetailRepository itemDetailRepository;
//    @Autowired
//    private OrderItemRepository orderItemRepository;
//    @Autowired
//    private BrandRepository brandRepository;
//    @Autowired
//    private DeliveryRepository deliveryRepository;
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @Commit
//    @Test
//    public void createOrder() throws Exception {
//            Category category1 = new Category();
//            category1.setParent(null);
//            category1.setName("상의");
//        categoryRepository.save(category1);
//        categoryRepository.flush();
//
//        Category category2 = new Category();
//        category2.setParent(category1);
//        category2.setName("니트");
//        categoryRepository.save(category2);
//
//
//
//        //브랜드 등록
//            Brand brand = Brand.builder()
//                    .name("Brand")
//                    .build();
//            brandRepository.save(brand);
//
//            //아이템 등록
//            Item item = Item.builder()
//                    .name("니트")
//                    .brand(brand)
//                    .price(30000)
//                    .build();
//            //아이템 등록시에 카테고리에도 추가한다.
//        Optional<Category> categoryByName = categoryRepository.findCategoryByName("니트");
//        if(categoryByName.isPresent()){
//            Category category = categoryByName.get();
//            category.getItems().add(item); //변경 감지???
//        }
//
//        itemRepository.save(item);
//            ItemDetail itemDetail =ItemDetail.builder()
//                    .parentItem(item)
//                    .color("white")
//                    .size("XL")
//                    .stockQuantity(20)
//                    .build();
//            itemDetailRepository.save(itemDetail);
//
//
//            //회원 등록
//            Customer customer = Customer.builder()
//                    .name("customer..")
//                    .address(new Address("city","street","zipcode"))
////                    .active(false)
//                    .brand(brandRepository.getOne(brand.getId()))
//                    .email("customer"+"@naver.com")
//                    .phoneNumber("010-4444-4444")
//                    .build();
//            customerRepository.save(customer);
//
//            OrderItem orderItem = OrderItem.createOrderItem(itemDetail,30000,2);
//            orderItemRepository.save(orderItem);
//
//            Delivery delivery = new Delivery();
//            delivery.setAddress(new Address("city","street","zipcode"));
//            delivery.setStatus(DeliveryStatus.READY);
//            deliveryRepository.save(delivery);
//
//            Order order = Order.createOrder(customer,delivery,orderItem );
//            orderRepository.save(order);
//
//
//
//
//        Order one = orderRepository.getOne(order.getId());
//        List<OrderItem> orderItems = one.getOrderItems();
//        for (OrderItem orderItem1 : orderItems) {
//            System.out.println("주문한 상품 색상 = " + orderItem1.getItemDetail().getColor());
//            System.out.println("주문한 상품 사이즈 = " + orderItem1.getItemDetail().getSize());
//            System.out.println("주문한 상품 남은 수량 = " + orderItem1.getItemDetail().getStockQuantity());
//            System.out.println("주문한 상품 부모상품 이름 = " + orderItem1.getItemDetail().getParentItem().getName());
//            System.out.println("주문한 상품 브랜드 = " + orderItem1.getItemDetail().getParentItem().getBrand());
//            System.out.println("주문한 상품 가격 = " + orderItem1.getItemDetail().getParentItem().getPrice());
//        }
//
//    }
//
//
//
//}
