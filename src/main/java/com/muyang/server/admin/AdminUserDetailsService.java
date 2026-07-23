package com.muyang.server.admin;

import com.muyang.server.auth.User;
import com.muyang.server.auth.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** 관리자 폼로그인용 — users 테이블에서 role=ADMIN 인 계정만 인증 */
@Service
@RequiredArgsConstructor
public class AdminUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User u = userRepository.findByEmail(email)
                .filter(x -> "ADMIN".equals(x.getRole()))
                .orElseThrow(() -> new UsernameNotFoundException("관리자 계정이 아닙니다"));
        return org.springframework.security.core.userdetails.User
                .withUsername(u.getEmail())
                .password(u.getPassword())
                .roles("ADMIN")
                .build();
    }
}
