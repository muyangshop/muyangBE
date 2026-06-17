package com.muyang.server.payment;

import com.muyang.server.payment.PaymentCancelRequest;
import com.muyang.server.payment.PaymentConfirmRequest;
import com.muyang.server.payment.PaymentReadyRequest;
import com.muyang.server.payment.PaymentReadyResponse;
import com.muyang.server.payment.Payment;
import com.muyang.server.auth.User;
import com.muyang.server.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private Long uid(User u) { return u == null ? null : u.getId(); }

    @PostMapping("/ready")
    public PaymentReadyResponse ready(@AuthenticationPrincipal User user,
                                      @Valid @RequestBody PaymentReadyRequest req) {
        return paymentService.ready(uid(user), req);
    }

    @PostMapping("/confirm")
    public Payment confirm(@AuthenticationPrincipal User user,
                           @Valid @RequestBody PaymentConfirmRequest req) {
        return paymentService.confirm(uid(user), req);
    }

    @GetMapping("/{id}")
    public Payment get(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return paymentService.get(uid(user), id);
    }

    @PostMapping("/{id}/cancel")
    public Payment cancel(@AuthenticationPrincipal User user, @PathVariable Long id, @Valid @RequestBody PaymentCancelRequest req){
        return paymentService.cancel(uid(user), id, req.reason());
    }
}