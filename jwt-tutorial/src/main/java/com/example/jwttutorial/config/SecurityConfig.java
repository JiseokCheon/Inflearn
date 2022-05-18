package com.example.jwttutorial.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity  // 기본적인 Web 보안을 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // h2 console 하위 모든 요청들과 파비콘 관련 요청은 security 로직을 수행하지 않도록 설정
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "favicon.ico"
                );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()// HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다는 의미
                .antMatchers("/api/hello").permitAll() // /api/hello에 대한 요청은 인증 없이 허용
                .anyRequest().authenticated();  // 나머지 요청들은 인증 필요
    }
}
