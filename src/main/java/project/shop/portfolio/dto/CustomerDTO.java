package project.shop.portfolio.dto;

import com.sun.istack.NotNull;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private String userId;
    private String password;
    private String name;
    private String birthday;
    private String city;
    private String street;
    private String zipcode;
    private String email;
    private String businessNumber;
    private String brandName;
    private String phoneNum1;
    private String phoneNum2;

    private String email_1;
    private String email_2;
    private String phoneNum1_1;
    private String phoneNum1_2;
    private String phoneNum1_3;
    private String phoneNum2_1;
    private String phoneNum2_2;
    private String phoneNum2_3;

    public void makePhoneNumberAndEmail(){
        this.phoneNum1 =phoneNum1_1+"-"+phoneNum1_2+"-"+phoneNum1_3;
        this.phoneNum2 =phoneNum2_1+"-"+phoneNum2_2+"-"+phoneNum2_3;
        this.email =email_1+"@"+email_2;
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
        int index = this.email.indexOf("@");

        this.email_1 = this.email.substring(0,index);
        this.email_2 = this.email.substring(index+1,this.email.length());




    }

}
