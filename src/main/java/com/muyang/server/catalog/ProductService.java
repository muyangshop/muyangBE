package com.muyang.server.catalog;

import com.muyang.server.catalog.SkuDetailView;
import com.muyang.server.catalog.ImageType;
import com.muyang.server.catalog.Sku;
import com.muyang.server.catalog.SkuAttribute;
import com.muyang.server.catalog.SkuImage;
import com.muyang.server.catalog.SkuRepository;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final SkuRepository productRepository;

    /** 카테고리·검색어 필터 목록 */
    public List<Sku> search(String cat, String q) {
        return productRepository.findAllByOrderBySortOrderAsc().stream()
                .filter(p -> cat == null || cat.isBlank() || p.getCat().equals(cat))
                .filter(p -> q == null || q.isBlank()
                        || p.getName().contains(q)
                        || p.getCat().contains(q)
                        || p.getPet().contains(q))
                .toList();
    }

    /** 홈 화면 인기 상품 (앞 4개) */
    public List<Sku> popular() {
        return productRepository.findTop4ByOrderBySortOrderAsc();
    }

    public Sku getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Unknown product: " + id));
    }

    /** 상세페이지용 — 상품 + 본문 + 사진(용도별 분류) + 스펙을 한 번에 조립 */
    public SkuDetailView getDetail(String id) {
        Sku sku = getById(id);
        List<SkuImage> images = sku.getImages() == null ? List.of() : sku.getImages();

        SkuImage main = images.stream()
                .filter(i -> i.getType() == ImageType.MAIN)
                .findFirst()
                .orElse(null);
        List<SkuImage> gallery = byType(images, ImageType.GALLERY);
        List<SkuImage> detailImages = byType(images, ImageType.DETAIL);

        List<SkuAttribute> attributes = sku.getAttributes() == null ? List.of()
                : sku.getAttributes().stream()
                        .sorted(Comparator.comparingInt(SkuAttribute::getSortOrder))
                        .toList();

        return new SkuDetailView(sku, sku.getDetail(), main, gallery, detailImages, attributes);
    }

    private List<SkuImage> byType(List<SkuImage> images, ImageType type) {
        return images.stream()
                .filter(i -> i.getType() == type)
                .sorted(Comparator.comparingInt(SkuImage::getSortOrder))
                .toList();
    }
}
