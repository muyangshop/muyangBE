package com.muyang.server.payment;

import com.muyang.server.payment.PaymentConfirmRequest;
import com.muyang.server.payment.PaymentReadyRequest;
import com.muyang.server.payment.PaymentReadyResponse;
import com.muyang.server.order.Order;
import com.muyang.server.payment.Payment;
import com.muyang.server.payment.PaymentProvider;
import com.muyang.server.payment.PaymentStatus;
import com.muyang.server.payment.PaymentGateway;
import com.muyang.server.order.OrderRepository;
import com.muyang.server.payment.PaymentRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final Map<PaymentProvider, PaymentGateway> gateways;

    /** 등록된 모든 PaymentGateway 구현체를 provider별로 매핑 (스프링이 List로 주입) */
    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          List<PaymentGateway> gatewayList) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.gateways = gatewayList.stream()
                .collect(Collectors.toMap(PaymentGateway::provider, Function.identity()));
    }
    /** 결제 취소 — PAID만 가능. PG 취소 후 Payment·Order를 CANCELED로 */
    public Payment cancel(Long userId, Long paymentId, String reason) {
        requireUser(userId);
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다"));
        if (!payment.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 결제만 취소할 수 있습니다");
        }
        if (payment.getStatus() != PaymentStatus.PAID) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "승인 완료된 결제만 취소할 수 있습니다");
        }
        gateway(payment.getProvider()).cancel(payment, reason);
        payment.setStatus(PaymentStatus.CANCELED);
        payment.setFailReason(reason); // 취소 사유 보관
        paymentRepository.save(payment);

        orderRepository.findById(payment.getOrderId()).ifPresent(o -> {
            o.setStatus("CANCELED");
            orderRepository.save(o);
        });
        return payment;
    }
    /** 결제 시작 — 주문 검증 후 Payment(READY) 생성, PG 준비 호출 */
    public PaymentReadyResponse ready(Long userId, PaymentReadyRequest req) {
        requireUser(userId);
        Order order = orderRepository.findById(req.orderId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다"));
        if (!order.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 주문만 결제할 수 있습니다");
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 처리된 주문입니다");
        }
        // 같은 주문의 기존 READY 결제는 재사용하지 않고 새로 생성 (단순화)
        Payment payment = paymentRepository.save(Payment.builder()
                .orderId(order.getId())
                .userId(userId)
                .provider(req.provider())
                .status(PaymentStatus.READY)
                .amount(order.getTotal())
                .createdAt(Instant.now())
                .build());

        String orderName = orderName(order);
        String redirectUrl = gateway(req.provider()).ready(payment, orderName);
        paymentRepository.save(payment); // ready()에서 pgKey 채울 수 있음

        return new PaymentReadyResponse(payment.getId(), req.provider(),
                payment.getAmount(), orderName, redirectUrl);
    }

    /** 결제 승인 — PG 최종 승인 후 Payment·Order 상태 갱신 */
    public Payment confirm(Long userId, PaymentConfirmRequest req) {
        requireUser(userId);
        Payment payment = paymentRepository.findById(req.paymentId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다"));
        if (!payment.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 결제만 승인할 수 있습니다");
        }
        if (payment.getStatus() != PaymentStatus.READY) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 처리된 결제입니다");
        }
        try {
            String token = req.pgKey() != null ? req.pgKey() : req.pgToken();
            String pgKey = gateway(payment.getProvider()).confirm(payment, token, req.amount());
            payment.setPgKey(pgKey);
            payment.setStatus(PaymentStatus.PAID);
            payment.setApprovedAt(Instant.now());
        } catch (ResponseStatusException e) {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setFailReason(e.getReason());
            paymentRepository.save(payment);
            throw e;
        }
        paymentRepository.save(payment);

        orderRepository.findById(payment.getOrderId()).ifPresent(o -> {
            o.setStatus("PAID");
            orderRepository.save(o);
        });
        return payment;
    }

    @Transactional(readOnly = true)
    public Payment get(Long userId, Long paymentId) {
        Payment p = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "결제를 찾을 수 없습니다"));
        if (!p.getUserId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 결제만 조회할 수 있습니다");
        }
        return p;
    }

    private PaymentGateway gateway(PaymentProvider provider) {
        PaymentGateway g = gateways.get(provider);
        if (g == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "지원하지 않는 결제수단입니다");
        }
        return g;
    }

    private String orderName(Order order) {
        var items = order.getItems();
        if (items == null || items.isEmpty()) return "MuYang 주문";
        String first = items.get(0).getProduct().getName();
        return items.size() == 1 ? first : first + " 외 " + (items.size() - 1) + "건";
    }

    private void requireUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
        }
    }
}