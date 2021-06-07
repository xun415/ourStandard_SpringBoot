package project.shop.portfolio.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") //order는 db에러 발생
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //기본 생성자로 생성 막음
public class Order extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    //원래는 orderItem들을 persist먼저 한 다음에 order을 persist 해야함
    //cascade설정시엔 order만 persist하면 orderItem도 같이 됨.
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간
    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    //==연관관계 편의 메소드 ==
    //핵심적으로 컨트롤 하는 쪽에 연관관계 메소드가 있는게 좋음
    public void setCustomer(Customer customer){
        this.customer = customer;
        customer.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

//    //== 생성메소드 ==//
    public static Order createOrder(Customer customer, Delivery delivery, OrderItem... orderItems){
        Order order = new Order();
        order.setCustomer(customer);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //== 비지니스 로직 ==//
    //주문취소
    public void cancel(){
        if(delivery.getStatus() != DeliveryStatus.결제완료){
            throw new IllegalStateException("이미 배송시작된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }
    //== 조회 로직 ==//

    //전체 주문 가격 조회
    public int getTotalPrice(){
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }


}