package com.sunmoon.delivery.domain.delivery;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public class Delivery {

    private final String id;
    private final String orderId;
    private DeliveryStatus status;
    private final Instant createdAt;

    @JsonCreator
    public Delivery(
            @JsonProperty("id") String id,
            @JsonProperty("orderId") String orderId,
            @JsonProperty("status") DeliveryStatus status,
            @JsonProperty("createdAt") Instant createdAt) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Delivery(String orderId) {
        this(UUID.randomUUID().toString(), orderId, DeliveryStatus.ASSIGNED, Instant.now());
    }

    public String getId() {
        return id;
    }

    public String getOrderId() {
        return orderId;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
