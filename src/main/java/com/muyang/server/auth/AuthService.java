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
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;

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

    @Transactional
    public AuthResponse kakaoLogin(KakaoLoginRequest req) {
        KakaoUserInfo info;
        try {
            String accessToken = kakaoClient.getToken(req.code(), req.redirectUri());
            info = kakaoClient.getUserInfo(accessToken);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "카카오 인증에 실패했습니다");
        }
        if (info == null || info.id() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "카카오 사용자 정보를 가져오지 못했습니다");
        }

        String providerId = String.valueOf(info.id());
        User user = userRepository.findByProviderAndProviderId("KAKAO", providerId)
                .orElseGet(() -> {
                    KakaoUserInfo.KakaoAccount acc = info.kakaoAccount();
                    String nickname = (acc != null && acc.profile() != null && acc.profile().nickname() != null)
                            ? acc.profile().nickname() : "카카오회원";
                    String email = (acc != null) ? acc.email() : null;
                    if (email == null || email.isBlank()) {
                        email = "kakao_" + providerId + "@muyang.social"; // 이메일 미동의 시 합성
                    }
                    return userRepository.save(User.builder()
                            .email(email)
                            .password(passwordEncoder.encode("KAKAO_" + providerId)) // 소셜은 비번 미사용
                            .name(nickname)
                            .role("USER")
                            .grade("BRONZE")
                            .provider("KAKAO")
                            .providerId(providerId)
                            .onboarded(false)
                            .createdAt(Instant.now())
                            .build());
                });

        return new AuthResponse(jwtService.generate(user.getEmail()), UserView.of(user));
    }

    @Transactional
    public AuthResponse naverLogin(NaverLoginRequest req){
        NaverUserInfo info;
        try{
            info = naverClient.getUserInfo(req.accessToken());
        }catch(Exception e){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "네이버 인증에 실패했습니다.");
        }
        if( info == null || info.response() == null || info.response().id() == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "네이버 사용자 정보를 가져오지 못했습니다.");
        }
        NaverUserInfo.Response res = info.response();
        String providerId = res.id();
        User user = userRepository.findByProviderAndProviderId("NAVER", providerId)
                .orElseGet(() ->{
                    String name = (res.name() != null) ? res.name()
                            :(res.nickname() != null ? res.nickname() : "네이버회원");
                    String email = res.email();
                    if(email == null || email.isBlank()){
                        email = "naver_" + providerId + "@muyang.social";
                    }
                    return userRepository.save(User.builder()
                                    .email(email)
                                    .password(passwordEncoder.encode("NAVER_" + providerId))
                                    .name(name)
                                    .role("USER")
                                    .grade("BRONZE")
                                    .provider("NAVER")
                                    .providerId(providerId)
                                    .onboarded(false)
                                    .createdAt(Instant.now())
                            .build());
                });
        return new AuthResponse(jwtService.generate(user.getEmail()), UserView.of(user));
    }
}