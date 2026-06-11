package com.muyang.server.controller;

import com.muyang.server.dto.AddCartRequest;
import com.muyang.server.dto.CartView;
import com.muyang.server.dto.QtyRequest;
import com.muyang.server.service.CartService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public CartView get() {
        return cartService.getCart();
    }

    @PostMapping
    public CartView add(@RequestBody AddCartRequest request) {
        int qty = request.qty() == null ? 1 : request.qty();
        return cartService.add(request.productId(), qty);
    }

    @PatchMapping("/{productId}")
    public CartView setQty(@PathVariable String productId, @RequestBody QtyRequest request) {
        return cartService.setQty(productId, request.qty());
    }

    @DeleteMapping("/{productId}")
    public CartView remove(@PathVariable String productId) {
        return cartService.remove(productId);
    }
}
