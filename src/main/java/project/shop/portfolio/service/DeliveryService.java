package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Delivery;
import project.shop.portfolio.domain.DeliveryStatus;
import project.shop.portfolio.repository.DeliveryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    @Transactional
    public void modifyDeliveryStatus(Long id, String deliveryStatus){

        Optional<Delivery> byId = deliveryRepository.findById(id);
            Delivery delivery = byId.get();


        DeliveryStatus deliveryStatus1 = DeliveryStatus.valueOf(deliveryStatus);

        delivery.modifyStatus(deliveryStatus1);


    }
}
