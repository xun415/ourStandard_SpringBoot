package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingBrandManagerDTO {
    //고객아이디 고객이름 출생년도 연락처1 연락처2 사업자번호 이메일 브랜드명
    private Long id;
    private String userId;
    private String name;
    private String birthday;
    private String phoneNumber1;
    private String phoneNumber2;
    private String email;
    private String businessNumber;
    private String brandName;


}
