package com.muyang.server.order;
import com.muyang.server.cart.CartService;
import com.muyang.server.point.PointService;

import com.muyang.server.cart.CartView;
import com.muyang.server.order.Order;
import com.muyang.server.order.OrderItem;
import com.muyang.server.order.OrderRepository;
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
    private final PointService pointService;

    @Transactional(readOnly = true)
    public List<Order> getOrders(Long userId) {
        return userId == null ? List.of() : orderRepository.findByUserIdOrderByIdDesc(userId);
    }

    /** 현재 유저의 장바구니로 PENDING 주문 생성 후 장바구니 비우기 (결제 승인 시 PAID 전환) */
    public Order checkout(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
        CartView cart = cartService.getCart(userId);
        if (cart.items().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "장바구니가 비어 있습니다");
        }

        List<OrderItem> items = cart.items().stream()
                .map(l -> OrderItem.builder().product(l.product()).qty(l.qty()).build())
                .toList();

        Order order = Order.builder()
                .userId(userId)
                .items(items)
                .subtotal(cart.subtotal())
                .shipping(cart.shipping())
                .total(cart.total())
                .status("PENDING")
                .createdAt(Instant.now())
                .build();

        Order saved = orderRepository.save(order);
        cartService.clear(userId);
        pointService.record(userId, saved.getTotal() / 100, "주문적립(#" + saved.getId() + ")");
        return saved;
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long userId, Long orderId){
        if(userId == null){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"));
        if(!order.getUserId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 조회할 수 있습니다.");
        }
        return order;
    }
    public Order cancel(Long userId, Long orderId){
        Order order = getOrder(userId, orderId);
        if(!"PENDING".equals(order.getStatus())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "결제 대기 중인 주문만 취소할 수 있습니다 (현재: " + order.getStatus() + ")");
        }
        order.setStatus("CANCELED");
        return orderRepository.save(order);
    }
}
