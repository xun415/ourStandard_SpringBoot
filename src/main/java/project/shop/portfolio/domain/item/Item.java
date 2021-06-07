package project.shop.portfolio.domain.item;

import lombok.*;
import project.shop.portfolio.domain.Brand;
import project.shop.portfolio.domain.Category;
import project.shop.portfolio.domain.Review;
import project.shop.portfolio.exception.NotEnoughStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "brand")
public class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;

    //사이즈&컬러별 재고는 다르므로, 이부분은 상속받는 도메인에 추가한다.
    //private int stockQuantity;
    //private String size;
//    private String color;

    //카테고리
    @Builder.Default
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "item")
    private List<ItemImage> itemImageList = new ArrayList<>();


    //브랜드
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    public void modifyItem(String name, int price, Category category) {
        this.name =name;
        this.price =price;
        this.categories.remove(0);
        this.categories.add(category);
    }

//    //리뷰
//    @OneToMany(mappedBy = "item")
//    private List<Review> reviews = new ArrayList<>();

}
