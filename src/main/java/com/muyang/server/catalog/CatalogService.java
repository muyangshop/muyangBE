package com.muyang.server.catalog;

import com.muyang.server.catalog.Category;
import com.muyang.server.catalog.Promo;
import com.muyang.server.review.Review;
import com.muyang.server.catalog.CategoryRepository;
import com.muyang.server.catalog.PromoRepository;
import com.muyang.server.review.ReviewRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final PromoRepository promoRepository;
    private final ReviewRepository reviewRepository;

    public List<Category> categories() {
        return categoryRepository.findAllByOrderByIdAsc();
    }

    public List<Promo> promos() {
        return promoRepository.findAllByOrderByIdAsc();
    }

    public List<Review> reviews() {
        return reviewRepository.findAllByOrderByIdAsc();
    }
}
