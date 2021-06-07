package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BrandReviewDTO {
    private Long itemId;
    private Long reviewId;
    private String itemName;
    private String writer;
    private String text;
    private int grade;


}
