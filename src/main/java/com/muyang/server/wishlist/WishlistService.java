package com.muyang.server.wishlist;
import com.muyang.server.catalog.ProductService;

import com.muyang.server.wishlist.WishToggleResponse;
import com.muyang.server.catalog.Sku;
import com.muyang.server.wishlist.Wishlist;
import com.muyang.server.wishlist.WishlistItem;
import com.muyang.server.catalog.SkuRepository;
import com.muyang.server.wishlist.WishlistRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final SkuRepository productRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<Sku> getWishlist(Long userId) {
        if (userId == null) return List.of();
        Wishlist w = wishlistRepository.findByUserId(userId).orElse(null);
        if (w == null) return List.of();
        Set<String> ids = w.getItems().stream()
                .map(WishlistItem::getProductId).collect(Collectors.toSet());
        return productRepository.findAllByOrderBySortOrderAsc().stream()
                .filter(p -> ids.contains(p.getId())).toList();
    }

    public WishToggleResponse toggle(Long userId, String productId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
        productService.getById(productId);
        Wishlist w = getOrCreate(userId);
        WishlistItem existing = w.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst().orElse(null);
        boolean wished;
        if (existing != null) {
            w.getItems().remove(existing);
            wished = false;
        } else {
            w.getItems().add(WishlistItem.builder().productId(productId).build());
            wished = true;
        }
        wishlistRepository.save(w);
        return new WishToggleResponse(wished, getWishlist(userId));
    }

    private Wishlist getOrCreate(Long userId) {
        return wishlistRepository.findByUserId(userId)
                .orElseGet(() -> wishlistRepository.save(Wishlist.builder().userId(userId).build()));
    }
}