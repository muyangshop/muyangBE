package com.muyang.server.cart;

import com.muyang.server.cart.AddCartRequest;
import com.muyang.server.cart.CartView;
import com.muyang.server.cart.QtyRequest;
import com.muyang.server.auth.User;
import com.muyang.server.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private Long uid(User u) { return u == null ? null : u.getId();}

    @GetMapping
    public CartView get(@AuthenticationPrincipal User user){
        return cartService.getCart(uid(user));
    }
    @PostMapping
    public CartView add(@AuthenticationPrincipal User user, @RequestBody AddCartRequest request){
        int qty = request.qty() == null ? 1 : request.qty();
        return cartService.add(uid(user), request.productId(), qty);
    }
    @PatchMapping("/{productId}")
    public CartView setQty(@AuthenticationPrincipal User user, @PathVariable String productId, @RequestBody QtyRequest request){
        return cartService.setQty(uid(user), productId, request.qty());
    }
    @DeleteMapping("/{productId}")
    public CartView remove(@AuthenticationPrincipal User user, @PathVariable String productId) {
        return cartService.remove(uid(user), productId);
    }
}
