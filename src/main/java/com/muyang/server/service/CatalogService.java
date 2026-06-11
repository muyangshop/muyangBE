package com.muyang.server.service;

import com.muyang.server.entity.Category;
import com.muyang.server.entity.Promo;
import com.muyang.server.entity.Review;
import com.muyang.server.repository.CategoryRepository;
import com.muyang.server.repository.PromoRepository;
import com.muyang.server.repository.ReviewRepository;
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
