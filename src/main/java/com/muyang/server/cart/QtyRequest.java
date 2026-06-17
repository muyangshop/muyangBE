package com.muyang.server.cart;

/** PATCH /api/cart/{productId} 요청 바디 — 0 이하면 삭제 */
public record QtyRequest(int qty) {
}
