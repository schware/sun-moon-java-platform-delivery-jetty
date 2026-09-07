package com.sunmoon.delivery.infrastructure.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunmoon.delivery.domain.delivery.Delivery;
import com.sunmoon.delivery.domain.delivery.DeliveryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

// Postgres + JSONB, not a document database — see docs/adr in the parent
// sun-moon-java-platform repo for why (MongoDB doesn't run on the
// current homelab CPU: it requires AVX, which this box's Core i5 M 480
// doesn't have). The whole Delivery is stored as one JSONB blob keyed by
// id, with order_id pulled out as a plain indexed column for lookups —
// same access pattern a document store would give, on hardware that
// actually runs here.
@Repository
public class JdbcDeliveryRepository implements DeliveryRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ObjectMapper objectMapper;

    public JdbcDeliveryRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public Delivery save(Delivery delivery) {
        String json = writeJson(delivery);
        jdbcTemplate.update(
                "INSERT INTO deliveries (id, order_id, data) VALUES (?, ?, ?::jsonb) "
                        + "ON CONFLICT (id) DO UPDATE SET order_id = EXCLUDED.order_id, data = EXCLUDED.data",
                delivery.getId(), delivery.getOrderId(), json);
        return delivery;
    }

    private String writeJson(Delivery delivery) {
        try {
            return objectMapper.writeValueAsString(delivery);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize Delivery " + delivery.getId(), e);
        }
    }
}
