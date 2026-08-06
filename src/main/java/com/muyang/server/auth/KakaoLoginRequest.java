package com.muyang.server.auth;

public record KakaoLoginRequest(String code, String redirectUri) {
}
