package com.muyang.server.catalog;

import com.muyang.server.catalog.Sku;
import com.muyang.server.catalog.SkuAttribute;
import com.muyang.server.catalog.SkuDetail;
import com.muyang.server.catalog.SkuImage;
import java.util.List;

/** 상품 상세페이지 응답 — 기본 정보 + 본문 + 사진(용도별) + 스펙 */
public record SkuDetailView(
        Sku sku,
        SkuDetail detail,
        SkuImage mainImage,
        List<SkuImage> gallery,
        List<SkuImage> detailImages,
        List<SkuAttribute> attributes) {
}
