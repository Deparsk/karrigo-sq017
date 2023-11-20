package com.decagon.karrigobe.payload.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse {
    private Long transactionId;
    private String trackingNum;
    private String receipt;
    private String amount;
    private LocalDateTime time;
}