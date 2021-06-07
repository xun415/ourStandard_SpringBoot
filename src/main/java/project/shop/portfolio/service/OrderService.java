package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import project.shop.portfolio.domain.*;
import project.shop.portfolio.domain.item.ItemDetail;
import project.shop.portfolio.dto.OrderHistoryDTO;
import project.shop.portfolio.dto.OrderItemDTO;
import project.shop.portfolio.dto.PurchaseDTO;
import project.shop.portfolio.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final CustomerRepository memberRepository;
    private final ItemRepository itemRepository;
    private final ItemDetailRepository itemDetailRepository;
    private final DeliveryRepository deliveryRepository;

    public List<Order> getOrderListByUserId(Long userId){
        return orderRepository.findAllByUserId(userId);
    }

    @Transactional
    public Long order(Long customerId, Long itemDetailId, Delivery delivery, int count){
        //엔티티 조회
        Optional<Customer> byId = customerRepository.findById(customerId);
        Customer customer = byId.get();

        Optional<ItemDetail> byId1 = itemDetailRepository.findById(itemDetailId);
        ItemDetail itemDetail = byId1.get();

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(itemDetail, itemDetail.getParentItem().getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(customer, delivery, orderItem);
        //주문 저장
        orderRepository.save(order);

        //배송정보 생성&저장
        delivery.setStatus(DeliveryStatus.결제완료);
        delivery.setOrder(order);
        deliveryRepository.save(delivery);

        return order.getId();

    }

    /*
     *주문 취소
     */
    @Transactional
    public void cancleOrder(Long orderId){
        //주문 엔티티 조회
        Optional<Order> byId = orderRepository.findById(orderId);
        Order order = byId.get();
        //주문 취소
        order.cancel();
    }

    public List<OrderHistoryDTO> getOrderHistoryDTOListByUserId(Long customerId) {
        List<OrderHistoryDTO> orderHistoryDTOList = new ArrayList<>();

        List<Order> orderListByUserId = getOrderListByUserId(customerId);
        for (Order order : orderListByUserId) { //오더 리스트

            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) { //하나의 오더 안에 있는 오더 아이템들
                //아이템별로OrderHistoryDTO를 만든다
                OrderHistoryDTO orderHistoryDTO = OrderHistoryDTO.builder()
                        .orderId(order.getId())
                        .itemId(orderItem.getItemDetail().getParentItem().getId())
                        .orderDate(order.getOrderDate())
                        .orderPrice(order.getTotalPrice())
                        .orderStatus(order.getStatus().name().equals("CANCEL")?order.getStatus().name() :order.getDelivery().getStatus().name())
                        .itemName(orderItem.getItemDetail().getParentItem().getName())
                        .price(orderItem.getItemDetail().getParentItem().getPrice())
                        .quantity(orderItem.getCount())
                        .size(orderItem.getItemDetail().getSize())
                        .th_Img(orderItem.getItemDetail().getParentItem().getItemImageList().get(0).getThumbnailFileName())
                        .build();
                orderHistoryDTOList.add(orderHistoryDTO);
            }

        }
        return orderHistoryDTOList;
    }

    //셀러가 주문 목록을 조회하게 해줌(브랜드로 오더내역 조회)
    public List<PurchaseDTO> getBrandPurchaseList(Long brandId){
        List<PurchaseDTO> purchaseDTOList = new ArrayList<>();
        try {


            List<Object[]> idByBrandId = orderRepository.findIdByBrandId(brandId);
            for (Object[] objects : idByBrandId) {
                for (Object object : objects) {
                    Long orderId = Long.valueOf(String.valueOf(object));
                    Optional<Order> byId = orderRepository.findById(orderId);
                    Order order = byId.get();
                    order.getOrderItems();
                    System.out.println("order = " + order.getId());
                    PurchaseDTO purchaseDTO = PurchaseDTO.builder()
                            .deliveryId(order.getDelivery().getId())
                            .userId(order.getCustomer().getUserId())
                            .userName(order.getCustomer().getName())
                            .address(order.getDelivery().getAddress())
                            .phoneNum(order.getDelivery().getPhoneNumber())
                            .orderDate(order.getOrderDate())
                            .deliveryStatus(order.getStatus().name().equals("CANCEL")?order.getStatus().name() :order.getDelivery().getStatus().name())
                            .orderNumber(orderId)
                            .build();
                    purchaseDTOList.add(purchaseDTO);
                }
            }
                    return purchaseDTOList;
        }catch (NullPointerException e){
            e.printStackTrace();
            return  null;
        }


    }

    public List<OrderItemDTO> getOrderItemDTOList(Long orderNumber) {
    //셀러가 주문 목록을 조회한후, 오더 아이디를 클릭하면, 주문 아이템을 보게 해줌
        Optional<Order> byId = orderRepository.findById(orderNumber);
        Order order = byId.get();
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        List<OrderItem> orderItems = order.getOrderItems();
        for (OrderItem orderItem : orderItems) {

            OrderItemDTO orderItemDTO = OrderItemDTO.builder()
                    .orderItemId(orderItem.getId())
                    .itemName(orderItem.getItemDetail().getParentItem().getName())
                    .price(orderItem.getOrderPrice())
                    .size(orderItem.getItemDetail().getSize())
                    .quantity(orderItem.getCount())
                    .build();
            orderItemDTOList.add(orderItemDTO);
        }

        return orderItemDTOList;


    }

    //배송상태 적용할 수 있게 해줌




//    //검색
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAllByString(orderSearch);
//    }

}
