package com.example.userservice.security;

import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private UserService userService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private Environment env;

    @Autowired
    public WebSecurity(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.env = env;
    }


    // 여러 configure 중 권한 관련
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
//        http.authorizeRequests().antMatchers("/users/**").permitAll();
        http.authorizeRequests()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers("/error/**").permitAll()
                .antMatchers("/**")
                .hasIpAddress("172.30.1.39") // ip 변경 필요
                .and()
                .addFilter(getAuthenticationFilter());

        // h2에선 데이터가 프레임 별로 나눠져 있는데 그걸 무시해야 h2-console 사용가능
        http.headers().frameOptions().disable();
    }

    private AuthenticationFilter getAuthenticationFilter() throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager(), userService, env);

        // 생성자를 통해 AuthenticationManager 전달했기 때문에 아래 코드 필요 없음
//        authenticationFilter.setAuthenticationManager(authenticationManager());

        /**
         * AuthenticationManager는 ProvierManager를 구현한 클래스로써,
         * 인자로 전달받은 유저에 대한 인증 정보를 담고 있으며, 해당 인증 정보가 유효할 경우
         * UserDetailsService에서 적절한 Principal을 가지고 있는
         * Authentication 객체를 반환해 주는 역할을 하는 인증 공급자(Provider) 입니다.
         */

        return authenticationFilter;
    }

    // 인증 관련
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // userDetailService -> 사용자가 요청한 값으로 로그인 처리
        // select pwd from users where email = ?    <- 이 작업을 userDetailSerivce에서 처리
        // db_pwd(encrypted) == input_pwd(encrypted)    <- passwordEncoder
        auth.userDetailsService(userService).passwordEncoder(bCryptPasswordEncoder);

    }
}
