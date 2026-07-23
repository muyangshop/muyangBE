package com.muyang.server.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NaverUserInfo (String resultcode, String message, Response response){
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Response(String id, String email, String name, String nickname){
        
    }
}
