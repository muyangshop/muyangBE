package com.muyang.server.coupon;

import com.muyang.server.coupon.ClaimCouponRequest;
import com.muyang.server.coupon.UserCouponView;
import com.muyang.server.auth.User;
import com.muyang.server.coupon.CouponService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;

    private Long uid(User u) { return u == null ? null : u.getId(); }

    @GetMapping
    public List<UserCouponView> myCoupons(@AuthenticationPrincipal User user) {
        return couponService.myCoupons(uid(user));
    }

    @PostMapping("/claim")
    public UserCouponView claim(@AuthenticationPrincipal User user, @Valid @RequestBody ClaimCouponRequest req) {
        return couponService.claim(uid(user), req);
    }
}