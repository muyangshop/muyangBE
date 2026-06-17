package com.muyang.server.auth;

public record AuthResponse(String token, UserView user) {
}
