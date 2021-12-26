package com.project.restfulwebservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration // 설정 정보 로딩
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override // h2-console 인증처리 없이 사용하기 위한 옵션 설정
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/h2-console/**").permitAll(); // h2-console 허용
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("jenny")
                .password("{noop}password1") // 인코딩 없이 사용할 수 있도록 noop 옵션 부여 (no operation)
                .roles("USER");
    }

}
