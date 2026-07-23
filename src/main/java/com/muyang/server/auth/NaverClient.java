package com.muyang.server.auth;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NaverClient {
    private final RestClient rest = RestClient.create();
    public NaverUserInfo getUserInfo(String accessToken){
        return rest.get()
                .uri("https://openapi.naver.com/v1/nid/me")
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(NaverUserInfo.class);
    }
}
