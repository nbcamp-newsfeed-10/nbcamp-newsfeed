package com.sparta.nbcampnewsfeed.config;

import com.sparta.nbcampnewsfeed.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/posts/**").authenticated() // 인증된 사용자만 접근 허용
                                .anyRequest().permitAll() // 나머지 요청은 모두 허용
                )
                .formLogin(form -> form
                        .loginPage("/login")  // 사용자 정의 로그인 페이지
                        .permitAll()
                )
                .httpBasic(httpBasic -> httpBasic
                        .realmName("NBCamp")
                );

        // 필요한 경우 다른 방식으로 CSRF 설정을 비활성화하거나 관리할 수 있습니다.
        // http.csrf().disable();  // 이와 같이 비활성화할 수 있지만, 실제로는 필요한 경우에만 설정하세요.

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
