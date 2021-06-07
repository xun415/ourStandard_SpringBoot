package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemManageDTO {
    //id,상품명 상품이미지(썸네일), 카테고리1, 카테고리 2, 가격
    private String id;
    private String name;
    private String s_fileName;
    private String category1;
    private String category2;
    private int price;
}
