package com.muyang.server.controller;

import com.muyang.server.entity.Order;
import com.muyang.server.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> list() {
        return orderService.getOrders();
    }

    @PostMapping
    public Order checkout() {
        return orderService.checkout();
    }
}
