package com.muyang.server.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {
    private final RestClient rest = RestClient.create();
    public KakaoUserInfo getUserInfo(String accessToken){
        return rest.get()
                .uri("https://kapi.kakao.com/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoUserInfo.class);
    }
}
