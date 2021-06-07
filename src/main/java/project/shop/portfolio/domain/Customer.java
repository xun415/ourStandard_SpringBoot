package project.shop.portfolio.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends BaseEntity{
    @Id
    @GeneratedValue
    @Column(name = "customer_id")
    private Long id;

    private String userId;

    private String password;

    private String name;

    private String birthDay;

    private String phoneNumber;

    private String phoneNumber2;

    private String email;

    private String businessNumber;

//    @Enumerated(EnumType.STRING)
//    private Role role;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roleSet = new HashSet<>();

    public void addCustomerRole(Role role){
        roleSet.add(role);
    }


    //    브랜드 정보 (브랜드 회원일 경우 브랜드 아이디를 가지고 있음)
    @JoinColumn(name = "brand_id",nullable = true)
    @ManyToOne(fetch = FetchType.LAZY)
    private Brand brand;

    @Embedded
    private Address address;

    //주문
    @Builder.Default
    @OneToMany(mappedBy="customer",fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    //장바구니
    @Builder.Default
    @OneToMany(mappedBy = "customer",fetch = FetchType.LAZY)
    private List<Cart> carts = new ArrayList<>();



    public void modifyPasswordAddressAndPhoneNumber(String password,Address address, String phoneNumber, String phoneNumber2) {
        if(password!=null){
            if(password.length()>4){
                this.password = password;
            }
        }
        System.out.println("parameter address = " + address);
        System.out.println("(beforeChange)this.address = " + this.address);
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.phoneNumber2 = phoneNumber2;
        System.out.println("(afterChange)this.address = " + this.address);
    }

    public void changePassword(String password) {
        this.password = password;
    }


    public void setEncodedPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void deleteCustomer() {
        //추후 복구를 위해 이름과 생년월일만 남겨두기?
        this.address = null;
        this.phoneNumber ="";
        this.phoneNumber2 ="";
        this.email="";
    }

    public void deleteBusinessNumber() {
        this.businessNumber = null;
    }
}
