package com.sunmoon.delivery.transport.http;

import com.sunmoon.delivery.domain.delivery.Delivery;
import com.sunmoon.delivery.domain.delivery.DeliveryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRepository deliveryRepository;

    @Test
    void createsDelivery() throws Exception {
        Delivery saved = new Delivery("order-1");
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(saved);

        mockMvc.perform(post("/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":\"order-1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderId").value("order-1"))
                .andExpect(jsonPath("$.status").value("ASSIGNED"));
    }

    @Test
    void rejectsBlankOrderId() throws Exception {
        mockMvc.perform(post("/deliveries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"orderId\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
