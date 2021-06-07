package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterItemDTO {
    private String pp_Category1;
    private String pp_Category2;
    private String pp_Name;
    private String price;

    private MultipartFile[] m_Img;

}
