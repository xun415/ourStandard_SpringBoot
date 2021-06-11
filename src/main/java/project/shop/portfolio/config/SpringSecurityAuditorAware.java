package project.shop.portfolio.config;

import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import project.shop.portfolio.security.dto.CustomerAuthDTO;

import javax.annotation.Resource;
import java.util.Optional;
@Component
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (null == authentication || !authentication.isAuthenticated()) {
            return null;
        }

        return Optional.of((String)authentication.getPrincipal());
    }


}


