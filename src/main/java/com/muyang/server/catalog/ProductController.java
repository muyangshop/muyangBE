package com.muyang.server.catalog;

import com.muyang.server.catalog.SkuDetailView;
import com.muyang.server.catalog.Sku;
import com.muyang.server.catalog.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<Sku> list(
            @RequestParam(required = false) String cat,
            @RequestParam(required = false) String q) {
        return productService.search(cat, q);
    }

    @GetMapping("/popular")
    public List<Sku> popular() {
        return productService.popular();
    }

    @GetMapping("/{id}")
    public Sku get(@PathVariable String id) {
        return productService.getById(id);
    }

    /** 상세페이지 — 사진·본문·스펙까지 한 번에 */
    @GetMapping("/{id}/detail")
    public SkuDetailView detail(@PathVariable String id) {
        return productService.getDetail(id);
    }
}
