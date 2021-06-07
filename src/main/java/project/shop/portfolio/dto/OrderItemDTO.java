package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class OrderItemDTO {
    private Long orderItemId;

    private String itemName;
    private int quantity;
    private int price;
    private String size;

}
