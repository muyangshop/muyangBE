package com.muyang.server.auth;

import com.muyang.server.auth.User;
public record UserView(Long id, String email, String name, String role,
                       boolean onboarded, boolean marketingAgreed, String grade) {
    public static UserView of(User u) {
        return new UserView(u.getId(), u.getEmail(), u.getName(), u.getRole(),
                u.isOnboarded(), u.isMarketingAgreed(), u.getGrade());
    }
}