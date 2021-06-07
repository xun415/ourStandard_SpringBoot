package project.shop.portfolio.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import project.shop.portfolio.security.handler.CustomerLoginSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.
            csrf()
                .disable();
//                .authorizeRequests()
//                .antMatchers("/admin/**").hasRole("Admin")
//                .antMatchers("/customer/**").hasRole("Customer")
//                .antMatchers("/brand/**").hasRole("BrandManager")
//                .anyRequest().permitAll();
            http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/customer/login") //form의 action 경로
                .successHandler(successHandler())
                .and()
        .logout()
            .logoutUrl("/logout")
                .logoutSuccessUrl("/login");


//        .logoutSuccessUrl("/login");
//        http
//                .csrf().disable()
//                .authorizeRequests()
//                .antMatchers("/sample/all").permitAll()
//                .antMatchers("/sample/member").hasRole("Customer");
//        http.formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/customer/login") //form의 action 경로
//                .successHandler(successHandler())
//                .and()
//        .logout()
//            .logoutUrl("/logout")
//        .logoutSuccessUrl("/login");
//
//
//        http.formLogin().successHandler(successHandler());
//
//        http.logout();
    }
    @Bean
    public CustomerLoginSuccessHandler successHandler(){
        return new CustomerLoginSuccessHandler();
    }


}
