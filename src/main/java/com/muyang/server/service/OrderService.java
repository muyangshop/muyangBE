package com.muyang.server.service;

import com.muyang.server.dto.CartView;
import com.muyang.server.entity.Order;
import com.muyang.server.entity.OrderItem;
import com.muyang.server.repository.OrderRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartService cartService;

    @Transactional(readOnly = true)
    public List<Order> getOrders() {
        return orderRepository.findAllByOrderByIdDesc();
    }

    /** 현재 장바구니로 주문 생성 후 장바구니 비우기 */
    public Order checkout() {
        CartView cart = cartService.getCart();
        if (cart.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "장바구니가 비어 있습니다");
        }

        List<OrderItem> items = cart.items().stream()
                .map(l -> OrderItem.builder().product(l.product()).qty(l.qty()).build())
                .toList();

        Order order = Order.builder()
                .items(items)
                .subtotal(cart.subtotal())
                .shipping(cart.shipping())
                .total(cart.total())
                .createdAt(Instant.now())
                .build();

        Order saved = orderRepository.save(order);
        cartService.clear();
        return saved;
    }
}
