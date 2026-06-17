package com.muyang.server.review;
import com.muyang.server.catalog.ProductService;

import com.muyang.server.review.ReviewRequest;
import com.muyang.server.review.Review;
import com.muyang.server.auth.User;
import com.muyang.server.review.ReviewRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductService productService;
    @Transactional(readOnly = true)
    public List<Review> byProduct(String skuId) {
        return reviewRepository.findBySkuIdOrderByIdDesc(skuId);
    }
    public Review create(User user, String skuId, ReviewRequest req) {
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
        productService.getById(skuId); // 상품 존재 검증
        Review review = Review.builder()
                .skuId(skuId)
                .userId(user.getId())
                .name(user.getName())
                .pet("회원")
                .rating(req.rating())
                .text(req.text())
                .createdAt(Instant.now())
                .build();
        return reviewRepository.save(review);
    }
}