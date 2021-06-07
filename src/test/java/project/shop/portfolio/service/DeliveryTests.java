package project.shop.portfolio.service;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Delivery;
import project.shop.portfolio.domain.DeliveryStatus;
import project.shop.portfolio.repository.DeliveryRepository;

import java.util.Optional;

@SpringBootTest
//@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@Transactional
public class DeliveryTests {
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void modifyDeliveryStatus() throws Exception {
        DeliveryStatus 상품준비중 = DeliveryStatus.valueOf("상품준비중");
        System.out.println("상품준비중 = " + 상품준비중);

//        deliveryService.modifyDeliveryStatus(33L,"상품준비중");
//
//        Optional<Delivery> byId = deliveryRepository.findById(33L);
//        Delivery delivery = byId.get();
//        System.out.println("delivery.getStatus() = " + delivery.getStatus());
//
//        Assertions.assertThat(delivery.getStatus().name()).isEqualTo("상품준비중");
    }
}
