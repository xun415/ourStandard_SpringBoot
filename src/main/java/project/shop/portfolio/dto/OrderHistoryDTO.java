package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.shop.portfolio.domain.DeliveryStatus;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class OrderHistoryDTO {

/*
                        주문일자 상품정보(사진,이름,사이즈) 가격 수량 주문금액 주문상태
 */
    private Long orderId;
    private Long itemId;
    private LocalDateTime orderDate;
    private String th_Img;
    private String itemName;
    private String size;
    private int price;
    private int quantity;
    private int orderPrice;
    private String orderStatus;

}
