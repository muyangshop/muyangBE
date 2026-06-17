package com.muyang.server.pet;

import jakarta.validation.constraints.NotBlank;

public record PetRequest(
        @NotBlank String name,
        @NotBlank String species,
        Integer age){
}
