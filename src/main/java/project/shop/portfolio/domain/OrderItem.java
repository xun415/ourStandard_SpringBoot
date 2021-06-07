package project.shop.portfolio.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.shop.portfolio.domain.item.ItemDetail;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_detail_id")
    private ItemDetail itemDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격
    private int count; //주문 수량

    //==생성 메소드==//
    public static OrderItem createOrderItem(ItemDetail item, int orderPrice, int count){//추후 할인 가능성을 위해 가격도 받아줌줌
        OrderItem orderItem = new OrderItem();
        orderItem.setItemDetail(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }
//    protected OrderItem(){ }//생성자로 생성하지 못하게 막기.@NoArgsConstructor(access = AccessLevel.PROTECTED) 랑 동일

    //== 비지니스 로직 ==//
    public void cancel() {
        getItemDetail().addStock(count);
    }

    //==조회 로직 ==//
    //주문상품 전체 가격 조회
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }

}
