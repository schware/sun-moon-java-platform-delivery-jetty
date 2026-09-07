package com.sunmoon.delivery.domain.delivery;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document("deliveries")
public class Delivery {

    @Id
    private String id;

    @Indexed
    private String orderId;

    private DeliveryStatus status;
    private Instant createdAt;

    protected Delivery() {
        // for Spring Data MongoDB
    }

    public Delivery(String orderId) {
        this.orderId = orderId;
        this.status = DeliveryStatus.ASSIGNED;
        this.createdAt = Instant.now();
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
