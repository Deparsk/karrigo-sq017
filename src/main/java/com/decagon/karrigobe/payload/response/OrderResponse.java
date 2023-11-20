package com.decagon.karrigobe.payload.response;


import com.decagon.karrigobe.entities.enums.OrderStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse implements Serializable {
    private Long orderId;
    private OrderStatus status;
    private LocalDateTime time;
}