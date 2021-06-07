package project.shop.portfolio.domain;

import lombok.*;
import project.shop.portfolio.domain.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString(exclude = "itemList")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Brand extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "brand_id")
    private Long id;

    private String name;

    private String businessNumber;

    //브랜드에 속한 아이템 리스트
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "brand")
    private List<Item> itemList = new ArrayList<>();


//    //
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "customer_id",nullable = true)
    @OneToMany(mappedBy = "brand",fetch = FetchType.LAZY)
    @Builder.Default
    private List<Customer> customerList = new ArrayList<>();

}
