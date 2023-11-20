package com.decagon.karrigobe.services.user_service.orders;

import com.decagon.karrigobe.payload.request.OrderDescriptionRequest;
import com.decagon.karrigobe.payload.response.TransactionResponse;

public interface OrderService {
    TransactionResponse createOrder(OrderDescriptionRequest orderDescriptionRequest);
    String cancelOder(Long orderId);
}
