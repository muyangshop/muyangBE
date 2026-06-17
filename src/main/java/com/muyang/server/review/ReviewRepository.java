package com.muyang.server.review;

import com.muyang.server.review.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByIdAsc();
    List<Review> findBySkuIdOrderByIdDesc(String skuId);
}
