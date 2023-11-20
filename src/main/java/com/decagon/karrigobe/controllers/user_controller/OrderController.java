package com.decagon.karrigobe.controllers.user_controller;

import com.decagon.karrigobe.payload.request.OrderDescriptionRequest;
import com.decagon.karrigobe.payload.response.ApiResponse;
import com.decagon.karrigobe.payload.response.TransactionResponse;
import com.decagon.karrigobe.services.user_service.orders.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<TransactionResponse>> createOrder(@Valid @RequestBody OrderDescriptionRequest descriptionRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("Order created successfully", orderService.createOrder(descriptionRequest)));
    }

    @GetMapping("/cancel")
    public ResponseEntity<ApiResponse<String>> cancelOrder(@RequestParam("orderId") Long orderId){
        return ResponseEntity.ok().body(new ApiResponse<>(orderService.cancelOder(orderId)));
    }
}
