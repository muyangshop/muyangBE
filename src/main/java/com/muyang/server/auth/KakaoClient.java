package com.muyang.server.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {
    private final RestClient rest = RestClient.create();

    @Value("${kakao.rest-api-key:}")
    private String restApiKey;

    @Value("${kakao.client-secret:}")
    private String clientSecret;

    /** 인가 코드(code) → 액세스 토큰 교환 */
    public String getToken(String code, String redirectUri) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", "authorization_code");
        form.add("client_id", restApiKey);
        form.add("redirect_uri", redirectUri);
        form.add("code", code);
        if (clientSecret != null && !clientSecret.isBlank()) {
            form.add("client_secret", clientSecret);
        }
        KakaoTokenResponse res = rest.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(KakaoTokenResponse.class);
        if (res == null || res.accessToken() == null) {
            throw new IllegalStateException("카카오 토큰 발급 실패");
        }
        return res.accessToken();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return rest.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserInfo.class);
    }
}
