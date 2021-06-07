package project.shop.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.shop.portfolio.domain.Address;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDeliveryDTO {
    private String name;
    private String phoneNum1;
    private String phoneNum2;
    private Address address;

    private String phoneNum1_1;
    private String phoneNum1_2;
    private String phoneNum1_3;
    private String phoneNum2_1;
    private String phoneNum2_2;
    private String phoneNum2_3;

    public void makePhoneNumberAndEmail(){
        this.phoneNum1 =phoneNum1_1+"-"+phoneNum1_2+"-"+phoneNum1_3;
        this.phoneNum2 =phoneNum2_1+"-"+phoneNum2_2+"-"+phoneNum2_3;
    }

    public void makeDTOPhoneNumberAndEmail(){
        this.phoneNum1_1 = this.phoneNum1.substring(0,3);
        this.phoneNum1_2 = this.phoneNum1.substring(4,8);
        this.phoneNum1_3 = this.phoneNum1.substring(9,phoneNum1.length());
        if(phoneNum2!=null){
            if(phoneNum2.length()==phoneNum1.length()){
                this.phoneNum2_1 = this.phoneNum2.substring(0,3);
                this.phoneNum2_2 = this.phoneNum2.substring(4,8);
                this.phoneNum2_3 = this.phoneNum2.substring(9,phoneNum1.length());
            }
        }




    }
}
