package com.muyang.server.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 4, max = 64) String password,
        @NotBlank String name,
        @AssertTrue(message = "만 14세 이상 약관에 동의해야 합니다") boolean agreeAge,
        @AssertTrue(message = "이용약관에 동의해야 합니다") boolean agreeTos,
        @AssertTrue(message = "개인정보 수집·이용에 동의해야 합니다") boolean agreePrivacy,
        boolean agreeMarketing) {
}