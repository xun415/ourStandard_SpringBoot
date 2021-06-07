package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderFromDTO {
    private Long itemDetail_id;
    private String size;
    private String itemName;
    private Integer price;
    private Integer quantity;
    private String thumbImgName;
    private String color;
}
