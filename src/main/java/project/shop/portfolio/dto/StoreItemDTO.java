package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.shop.portfolio.domain.Review;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StoreItemDTO {
    private String parentId;
    private String name;
    private int price;
    private String Img;
    private String LongImg;

    @Builder.Default
    private List<ItemOptionDTO> optionDTOList = new ArrayList<>();



}
