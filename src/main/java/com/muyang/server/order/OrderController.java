package com.muyang.server.order;

import com.muyang.server.order.Order;
import com.muyang.server.auth.User;
import com.muyang.server.order.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private Long uid(User u) { return u == null ? null : u.getId(); }
    @GetMapping
    public List<Order> list(@AuthenticationPrincipal User user) {
        return orderService.getOrders(uid(user));
    }
    @PostMapping
    public Order checkout(@AuthenticationPrincipal User user) {
        return orderService.checkout(uid(user));
    }
    @GetMapping("/{id}")
    public Order get(@AuthenticationPrincipal User user, @PathVariable Long id){ return orderService.getOrder(uid(user), id);}
    @PostMapping("/{id}/cancel")
    public Order cancel(@AuthenticationPrincipal User user, @PathVariable Long id){ return orderService.cancel(uid(user), id);}

}
