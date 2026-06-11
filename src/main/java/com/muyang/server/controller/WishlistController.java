package com.muyang.server.controller;

import com.muyang.server.dto.WishToggleResponse;
import com.muyang.server.entity.Product;
import com.muyang.server.service.WishlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public List<Product> get() {
        return wishlistService.getWishlist();
    }

    @PostMapping("/{productId}/toggle")
    public WishToggleResponse toggle(@PathVariable String productId) {
        return wishlistService.toggle(productId);
    }
}
