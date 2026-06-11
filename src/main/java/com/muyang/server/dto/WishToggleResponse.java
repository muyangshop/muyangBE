package com.muyang.server.dto;

import com.muyang.server.entity.Product;
import java.util.List;

/** 찜 토글 응답 — 토글 결과 + 전체 찜 목록 */
public record WishToggleResponse(boolean wished, List<Product> wishlist) {
}
