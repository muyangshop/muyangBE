package com.muyang.server.point;

import com.muyang.server.point.PointBalance;
import com.muyang.server.auth.User;
import com.muyang.server.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    @GetMapping
    public PointBalance get(@AuthenticationPrincipal User user){
        return pointService.get(user == null ? null : user.getId());
    }
}
