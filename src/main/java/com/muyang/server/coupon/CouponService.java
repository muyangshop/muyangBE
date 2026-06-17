package com.muyang.server.coupon;

import com.muyang.server.coupon.ClaimCouponRequest;
import com.muyang.server.coupon.UserCouponView;
import com.muyang.server.coupon.Coupon;
import com.muyang.server.coupon.UserCoupon;
import com.muyang.server.coupon.CouponRepository;
import com.muyang.server.coupon.UserCouponRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    @Transactional(readOnly = true)
    public List<UserCouponView> myCoupons(Long userId) {
        requireUser(userId);
        return userCouponRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(uc -> {
                    Coupon c = couponRepository.findById(uc.getCouponId()).orElse(null);
                    return c == null ? null : UserCouponView.of(uc, c);
                })
                .filter(v -> v != null)
                .toList();
    }

    public UserCouponView claim(Long userId, ClaimCouponRequest req) {
        requireUser(userId);
        Coupon coupon = couponRepository.findByCodeAndActiveTrue(req.code())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 쿠폰 코드예요"));
        if (userCouponRepository.existsByUserIdAndCouponId(userId, coupon.getId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 받은 쿠폰이에요");
        }
        UserCoupon uc = userCouponRepository.save(UserCoupon.builder()
                .userId(userId)
                .couponId(coupon.getId())
                .used(false)
                .issuedAt(Instant.now())
                .build());
        return UserCouponView.of(uc, coupon);
    }

    private void requireUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
    }
}