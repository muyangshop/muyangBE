package com.muyang.server.cart;

import com.muyang.server.catalog.Sku;

/** 장바구니 한 줄 — 상품 + 수량 */
public record CartLine(Sku product, int qty) {
}
