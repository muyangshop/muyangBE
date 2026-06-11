package com.muyang.server.dto;

import java.util.List;

/** 장바구니 응답 — 합계·배송비·무료배송 진행도 포함 */
public record CartView(
        List<CartLine> items,
        int count,
        int subtotal,
        int shipping,
        int total,
        int shipFree,
        int remainingForFreeShip) {
}
