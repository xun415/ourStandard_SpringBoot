package project.shop.portfolio.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery",fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //결제완료 상품준비중 배송시작 배송완료

    public void modifyStatus(DeliveryStatus deliveryStatus1) {
        this.status =deliveryStatus1;
    }
}
