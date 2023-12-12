package com.example.board.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception { // spring security를 쓰기 위한 설정

        httpSecurity.csrf(AbstractHttpConfigurer::disable)
                    .cors(AbstractHttpConfigurer::disable)

                    .authorizeHttpRequests(request->request // Http 요청이 들어오면 검증 절차를 거치는 과정
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/img/**").permitAll()
                        .anyRequest().authenticated()
                    ).formLogin(form->form // form 로그인 창 설정
                        .loginPage("/board/login")
                        .defaultSuccessUrl("/", true) // 로그인 성공하면 어디로 이동할지
                        .permitAll()
                    ).logout(Customizer.withDefaults()) // 기본 설정 따름
                    .oauth2Login(Customizer.withDefaults()); // oauth2Login 창 띄움

        return httpSecurity.build();

    }

}