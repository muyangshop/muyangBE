package com.muyang.server.cart;
import com.muyang.server.catalog.ProductService;

import com.muyang.server.cart.CartLine;
import com.muyang.server.cart.CartView;
import com.muyang.server.cart.Cart;
import com.muyang.server.cart.CartItem;
import com.muyang.server.cart.CartRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {

    public static final int SHIP_FREE = 30_000;
    public static final int SHIP_FEE = 3_000;

    private final CartRepository cartRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public CartView getCart(Long userId) {
        if (userId == null) return emptyView();
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        return toView(cart == null ? List.of() : cart.getItems());
    }

    public CartView add(Long userId, String productId, int qty) {
        requireUser(userId);
        productService.getById(productId);
        Cart cart = getOrCreate(userId);
        CartItem item = find(cart, productId);
        setItemQty(cart, item, productId, (item == null ? 0 : item.getQty()) + qty);
        cartRepository.save(cart);
        return toView(cart.getItems());
    }

    public CartView setQty(Long userId, String productId, int qty) {
        requireUser(userId);
        Cart cart = getOrCreate(userId);
        setItemQty(cart, find(cart, productId), productId, qty);
        cartRepository.save(cart);
        return toView(cart.getItems());
    }

    public CartView remove(Long userId, String productId) {
        requireUser(userId);
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null) return emptyView();
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        cartRepository.save(cart);
        return toView(cart.getItems());
    }

    public void clear(Long userId) {
        cartRepository.findByUserId(userId).ifPresent(c -> {
            c.getItems().clear();
            cartRepository.save(c);
        });
    }

    private Cart getOrCreate(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(Cart.builder().userId(userId).build()));
    }

    private CartItem find(Cart cart, String productId) {
        return cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst().orElse(null);
    }

    private void setItemQty(Cart cart, CartItem item, String productId, int qty) {
        if (qty <= 0) {
            if (item != null) cart.getItems().remove(item);
            return;
        }
        if (item == null) cart.getItems().add(CartItem.builder().productId(productId).qty(qty).build());
        else item.setQty(qty);
    }

    private CartView toView(List<CartItem> items) {
        List<CartLine> lines = items.stream()
                .map(it -> new CartLine(productService.getById(it.getProductId()), it.getQty()))
                .toList();
        int count = lines.stream().mapToInt(CartLine::qty).sum();
        int subtotal = lines.stream().mapToInt(l -> l.product().getPrice() * l.qty()).sum();
        int shipping = (subtotal == 0 || subtotal >= SHIP_FREE) ? 0 : SHIP_FEE;
        return new CartView(lines, count, subtotal, shipping, subtotal + shipping,
                SHIP_FREE, Math.max(0, SHIP_FREE - subtotal));
    }

    private CartView emptyView() {
        return new CartView(List.of(), 0, 0, 0, 0, SHIP_FREE, SHIP_FREE);
    }

    private void requireUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
    }
}