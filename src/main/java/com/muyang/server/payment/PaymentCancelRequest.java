package com.muyang.server.payment;

import jakarta.validation.constraints.NotBlank;

public record PaymentCancelRequest(@NotBlank String reason) {
}
