package com.muyang.server.catalog;

import com.muyang.server.catalog.Category;
import com.muyang.server.catalog.Promo;
import com.muyang.server.review.Review;
import com.muyang.server.catalog.CatalogService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/categories")
    public List<Category> categories() {
        return catalogService.categories();
    }

    @GetMapping("/promos")
    public List<Promo> promos() {
        return catalogService.promos();
    }

    @GetMapping("/reviews")
    public List<Review> reviews() {
        return catalogService.reviews();
    }
}
