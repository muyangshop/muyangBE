package com.muyang.server.dto;

import com.muyang.server.entity.Product;

/** 장바구니 한 줄 — 상품 + 수량 */
public record CartLine(Product product, int qty) {
}
