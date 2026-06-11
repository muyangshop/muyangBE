package com.muyang.server.service;

import com.muyang.server.dto.CartLine;
import com.muyang.server.dto.CartView;
import com.muyang.server.entity.CartItem;
import com.muyang.server.entity.Product;
import com.muyang.server.repository.CartItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    /** 무료배송 기준 금액 */
    public static final int SHIP_FREE = 30_000;
    /** 기본 배송비 */
    public static final int SHIP_FEE = 3_000;

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public CartView getCart() {
        List<CartLine> lines = cartItemRepository.findAll().stream()
                .map(item -> new CartLine(productService.getById(item.getProductId()), item.getQty()))
                .toList();

        int count = lines.stream().mapToInt(CartLine::qty).sum();
        int subtotal = lines.stream()
                .mapToInt(l -> l.product().getPrice() * l.qty())
                .sum();
        int shipping = (subtotal == 0 || subtotal >= SHIP_FREE) ? 0 : SHIP_FEE;

        return new CartView(
                lines,
                count,
                subtotal,
                shipping,
                subtotal + shipping,
                SHIP_FREE,
                Math.max(0, SHIP_FREE - subtotal));
    }

    public CartView add(String productId, int qty) {
        Product product = productService.getById(productId); // 존재 검증
        int next = cartItemRepository.findById(product.getId())
                .map(CartItem::getQty)
                .orElse(0) + qty;
        applyQty(product.getId(), next);
        return getCart();
    }

    public CartView setQty(String productId, int qty) {
        productService.getById(productId); // 존재 검증
        applyQty(productId, qty);
        return getCart();
    }

    public CartView remove(String productId) {
        cartItemRepository.deleteById(productId);
        return getCart();
    }

    public void clear() {
        cartItemRepository.deleteAll();
    }

    private void applyQty(String productId, int qty) {
        if (qty <= 0) {
            cartItemRepository.deleteById(productId);
        } else {
            cartItemRepository.save(CartItem.builder().productId(productId).qty(qty).build());
        }
    }
}
