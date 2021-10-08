package com.springas.springas2.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    //패스워드 인코딩 Bean등록
    @Bean
    public BCryptPasswordEncoder encodePassword() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
// CSRF protection 을 비활성화
        http.csrf().disable();
        http.headers().frameOptions().disable();
        http.authorizeRequests()

                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/user/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/**").permitAll()
                .antMatchers("*").permitAll()
// 그 외 어떤 요청이든 '인증'
                .anyRequest().authenticated()
                .and()
// [로그인 기능]
                .formLogin()
// 로그인 View 제공 (GET /user/login)
                .loginPage("/user/login")
// 로그인 처리 (POST /user/login)
                .loginProcessingUrl("/user/login")
// 로그인 처리 후 성공 시 URL
                .defaultSuccessUrl("/")
// 로그인 처리 후 실패 시 URL
                .failureUrl("/user/login?error")
                .permitAll()
                .and()
// [로그아웃 기능]
                .logout()
// 로그아웃 요청 처리 URL
                .logoutUrl("/user/logout")
                .logoutSuccessUrl("/")
                .permitAll();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}