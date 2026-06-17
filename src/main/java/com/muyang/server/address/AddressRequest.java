package com.muyang.server.address;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank String label,
        @NotBlank String recipient,
        @NotBlank String phone,
        @NotBlank String zipcode,
        @NotBlank String address1,
        String address2,
        boolean isDefault
) {
}
