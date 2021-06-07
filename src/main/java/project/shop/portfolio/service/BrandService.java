package project.shop.portfolio.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.shop.portfolio.domain.Brand;
import project.shop.portfolio.repository.BrandRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class BrandService {
    private final BrandRepository brandRepository;

    public String findBrandNameByBusinessNumber(String businessNumber){
         return brandRepository.findBrandNameByBusinessNumber(businessNumber);
    }

    public boolean registerBrand(String brandName, String businessNumber){
        Optional<Brand> result = brandRepository.findByBusinessNumber(businessNumber);
        if (result.isPresent()){
            return false;
        }else{
            //없으면 저장 후 true 리턴
            Brand brand = Brand.builder()
                    .businessNumber(businessNumber)
                    .name(brandName)
                    .build();
            brandRepository.save(brand);
            return true;
        }
    }
}
