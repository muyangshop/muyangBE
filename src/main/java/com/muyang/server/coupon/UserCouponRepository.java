package com.muyang.server.coupon;

import com.muyang.server.coupon.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    List<UserCoupon> findByUserIdOrderByIdDesc(Long userId);
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}