package com.muyang.server.payment;

import com.muyang.server.payment.PaymentProvider;
import jakarta.validation.constraints.NotNull;

public record PaymentReadyRequest(
        @NotNull Long orderId,
        @NotNull PaymentProvider provider
        ) {
}
