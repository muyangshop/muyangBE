package com.muyang.server.payment;

import jakarta.validation.constraints.NotNull;

public record PaymentConfirmRequest(
        @NotNull Long paymentId,
        String pgKey,
        String pgToken,
        Integer amount
) {
}
