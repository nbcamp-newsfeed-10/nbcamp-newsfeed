package com.sparta.nbcampnewsfeed.config;

import com.sparta.nbcampnewsfeed.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService, JwtUtil jwtUtil) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/auth/**").permitAll()  // 로그인과 회원가입은 누구나 접근 가능
                                .anyRequest().authenticated()  // 그 외의 요청은 인증 필요
                )
                .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)  // JWT 필터 추가
                .csrf(csrf -> csrf.disable());  // CSRF 비활성화

        return http.build();
    }
}
