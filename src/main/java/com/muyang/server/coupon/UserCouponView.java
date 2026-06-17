package com.muyang.server.coupon;

import com.muyang.server.coupon.Coupon;
import com.muyang.server.coupon.DiscountType;
import com.muyang.server.coupon.UserCoupon;

public record UserCouponView(
        Long userCouponId,
        String code,
        String name,
        DiscountType discountType,
        int discountValue,
        int minOrder,
        boolean used) {
    public static UserCouponView of(UserCoupon uc, Coupon c) {
        return new UserCouponView(uc.getId(), c.getCode(), c.getName(),
                c.getDiscountType(), c.getDiscountValue(), c.getMinOrder(), uc.isUsed());
    }
}