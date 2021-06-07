package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CommentsDTO {
    private Long commentId;
    private Long reviewId;

    private String itemName;
    private String text;
    private String writer;
}
