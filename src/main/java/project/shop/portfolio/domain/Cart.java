package project.shop.portfolio.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shop.portfolio.domain.item.Item;
import project.shop.portfolio.domain.item.ItemDetail;

import javax.persistence.*;
import java.lang.reflect.Member;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "cart_id")
    private Long id;

    //회원 id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    //item id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_detail_id")
    private ItemDetail itemDetail;

    //수량
    private int quantity;

    //가격???
    private int price;


}
