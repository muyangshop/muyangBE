package com.muyang.server.repository;

import com.muyang.server.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByIdAsc();
}
