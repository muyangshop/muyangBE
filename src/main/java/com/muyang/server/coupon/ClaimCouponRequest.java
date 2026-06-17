package com.muyang.server.coupon;

import jakarta.validation.constraints.NotBlank;

public record ClaimCouponRequest(@NotBlank String code) {
}
