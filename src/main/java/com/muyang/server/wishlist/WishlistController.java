package com.muyang.server.wishlist;

import com.muyang.server.wishlist.WishToggleResponse;
import com.muyang.server.catalog.Sku;
import com.muyang.server.auth.User;
import com.muyang.server.wishlist.WishlistService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private Long uid(User u){return u == null ? null : u.getId();}

    @GetMapping
    public List<Sku> get(@AuthenticationPrincipal User user){
        return wishlistService.getWishlist(uid(user));
    }
    @PostMapping("/{productId}/toggle")
    public WishToggleResponse toggle(@AuthenticationPrincipal User user, @PathVariable String productId){
        return wishlistService.toggle(uid(user), productId);
    }
}
