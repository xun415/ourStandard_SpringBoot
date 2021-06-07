package project.shop.portfolio.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
@Log4j2
@Getter
@Setter
@ToString
public class CustomerAuthDTO extends User {
    private String userId;

    private String name;

    //Security의 User상속후 User클래스의 생성자 호출.
    public CustomerAuthDTO(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = username;
        //password는 부모클래스를 사용.
    }
}
