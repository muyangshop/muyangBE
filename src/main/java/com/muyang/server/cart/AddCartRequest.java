package com.muyang.server.cart;

/** POST /api/cart 요청 바디 — qty 생략 시 1 */
public record AddCartRequest(String productId, Integer qty) {
}
