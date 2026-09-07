package com.sunmoon.delivery.transport.http;

import com.sunmoon.delivery.domain.delivery.CreateDeliveryRequest;
import com.sunmoon.delivery.domain.delivery.Delivery;
import com.sunmoon.delivery.domain.delivery.DeliveryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deliveries")
@Tag(name = "Deliveries", description = "Delivery assignment")
public class DeliveryController {

    private final DeliveryRepository deliveryRepository;

    public DeliveryController(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @PostMapping
    @Operation(
            summary = "Assign a delivery",
            description = "Called when a ticket is marked ready; creates a delivery record in "
                    + "ASSIGNED status for a courier to pick up.")
    @ApiResponse(responseCode = "201", description = "Delivery created")
    @ApiResponse(responseCode = "400", description = "Validation failed (blank orderId)")
    public ResponseEntity<Delivery> create(@Valid @RequestBody CreateDeliveryRequest request) {
        Delivery saved = deliveryRepository.save(new Delivery(request.orderId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
