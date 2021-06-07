package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.shop.portfolio.domain.Address;
import project.shop.portfolio.domain.DeliveryStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PurchaseDTO {
    private Long deliveryId;

    private String userId;
    private String userName;
    private Address address;
    private String phoneNum;
    private LocalDateTime orderDate;
    private String deliveryStatus;
    private Long orderNumber; //페이지에서 클릭하면 정보 페이지로 넘어가도록

}
