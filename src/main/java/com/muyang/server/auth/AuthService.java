package com.muyang.server.auth;

import com.muyang.server.auth.AuthResponse;
import com.muyang.server.auth.LoginRequest;
import com.muyang.server.auth.RegisterRequest;
import com.muyang.server.auth.UserView;
import com.muyang.server.auth.User;
import com.muyang.server.auth.UserRepository;
import java.time.Instant;
import java.util.Map;

import com.muyang.server.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 가입된 이메일입니다");
        }
        User user = User.builder()
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .name(req.name())
                .role("USER")
                .grade("BRONZE")
                .onboarded(false)
                .marketingAgreed(req.agreeMarketing())
                .agreedAt(java.time.Instant.now())
                .createdAt(Instant.now())
                .build();
        userRepository.save(user);
        return new AuthResponse(jwtService.generate(user.getEmail()), UserView.of(user));
    }

    public AuthResponse login(LoginRequest req) {
        // 존재하지 않음 / 비번 불일치 모두 같은 메시지 (사용자 열거 방지)
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다"));
        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다");
        }
        return new AuthResponse(jwtService.generate(user.getEmail()), UserView.of(user));
    }

    public UserView completeOnboarding(User user) {
        user.setOnboarded(true);
        userRepository.save(user);
        return UserView.of(user);
    }

    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email){
        return !userRepository.existsByEmail(email);
    }
}