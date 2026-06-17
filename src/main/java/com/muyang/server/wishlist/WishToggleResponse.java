package com.muyang.server.wishlist;

import com.muyang.server.catalog.Sku;
import java.util.List;

/** 찜 토글 응답 — 토글 결과 + 전체 찜 목록 */
public record WishToggleResponse(boolean wished, List<Sku> wishlist) {
}
