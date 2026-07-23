package com.muyang.server.auth;

import com.muyang.server.auth.AuthResponse;
import com.muyang.server.auth.LoginRequest;
import com.muyang.server.auth.RegisterRequest;
import com.muyang.server.auth.UserView;
import com.muyang.server.auth.User;
import com.muyang.server.auth.AuthService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/logout")
    public Map<String, String> logout() {
        // 무상태 JWT — 클라이언트가 토큰을 폐기하면 됩니다
        return Map.of("message", "로그아웃되었습니다. 클라이언트에서 토큰을 삭제하세요.");
    }

    @GetMapping("/me")
    public UserView me(@AuthenticationPrincipal User user) {
        return UserView.of(user);
    }

    @PostMapping("/onboarding/complete")
    public UserView completeOnboarding(@AuthenticationPrincipal User user) {
        return authService.completeOnboarding(user);
    }
    @GetMapping("/check-email")
    public Map<String, Boolean> checkEmail(@RequestParam String email){
        return Map.of("available", authService.isEmailAvailable(email));
    }
    @PostMapping("/kakao")
    public AuthResponse kakaoLogin(@RequestBody KakaoLoginRequest req){
        return authService.kakaoLogin(req);
    }
    @PostMapping("/naver")
    public AuthResponse naverLogin(@RequestBody NaverLoginRequest req){
        return authService.naverLogin(req);
    }
}