package com.muyang.server.payment;

import com.muyang.server.payment.PaymentProvider;

public record PaymentReadyResponse(
        Long paymentId,
        PaymentProvider provider,
        int amount,
        String orderName,
        String redirectUrl
) {
}
