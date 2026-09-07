package com.sunmoon.delivery.domain.delivery;

import jakarta.validation.constraints.NotBlank;

public record CreateDeliveryRequest(@NotBlank String orderId) {
}
