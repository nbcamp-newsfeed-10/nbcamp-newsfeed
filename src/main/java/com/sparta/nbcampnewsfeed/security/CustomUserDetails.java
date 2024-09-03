package com.sparta.nbcampnewsfeed.security;

import com.sparta.nbcampnewsfeed.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    // 사용자 권한 반환 (현재는 기본 권한으로 설정)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // 필요에 따라 권한을 추가
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // User 엔티티의 패스워드 반환
    }

    @Override
    public String getUsername() {
        return user.getUsername(); // User 엔티티의 유저네임 반환
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 여부 설정
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠김 여부 설정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 여부 설정
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 여부 설정
    }
}
