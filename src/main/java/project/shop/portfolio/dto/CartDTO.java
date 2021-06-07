package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CartDTO {
    private String cartId;
    private String itemDetailId;
    private int price;
    private int quantity;
    private String itemName;
    private String color;
    private String size;
    private String th_ImgName;
}
