package project.shop.portfolio.domain.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import project.shop.portfolio.exception.NotEnoughStockException;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ItemDetail {
    @Id
    @GeneratedValue
    @Column(name = "item_detail_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item parentItem;

    private String size;
    private String color;
    private int stockQuantity;
    //재고 증가
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }
    //재고 감소
    public void removeStock(int quantity){
        int restStock = this.stockQuantity - quantity;
        if (restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

    public void modifyOption(String size, String color, int quantity) {
        this.size = size;
        this.color = color;
        this.stockQuantity = quantity;
    }
}
