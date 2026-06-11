package com.muyang.server.service;

import com.muyang.server.dto.WishToggleResponse;
import com.muyang.server.entity.Product;
import com.muyang.server.entity.WishlistItem;
import com.muyang.server.repository.ProductRepository;
import com.muyang.server.repository.WishlistItemRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistService {

    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public List<Product> getWishlist() {
        Set<String> ids = wishlistItemRepository.findAll().stream()
                .map(WishlistItem::getProductId)
                .collect(Collectors.toSet());
        return productRepository.findAllByOrderBySortOrderAsc().stream()
                .filter(p -> ids.contains(p.getId()))
                .toList();
    }

    public WishToggleResponse toggle(String productId) {
        productService.getById(productId); // 존재 검증
        boolean wished;
        if (wishlistItemRepository.existsById(productId)) {
            wishlistItemRepository.deleteById(productId);
            wished = false;
        } else {
            wishlistItemRepository.save(WishlistItem.builder().productId(productId).build());
            wished = true;
        }
        return new WishToggleResponse(wished, getWishlist());
    }
}
