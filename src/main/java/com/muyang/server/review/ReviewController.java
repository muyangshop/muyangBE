package com.muyang.server.review;

import com.muyang.server.review.ReviewRequest;
import com.muyang.server.review.Review;
import com.muyang.server.auth.User;
import com.muyang.server.review.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{id}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    @GetMapping
    public List<Review> list(@PathVariable String id){
        return reviewService.byProduct(id);
    }
    @PostMapping
    public Review create(@AuthenticationPrincipal User user,
                         @PathVariable String id,
                         @Valid @RequestBody ReviewRequest req){
        return reviewService.create(user, id, req);
    }
}
