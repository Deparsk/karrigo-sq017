package com.decagon.karrigobe.payload.response;

import lombok.*;

@Getter

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLocationResponse {
    private Long locationId;
    private String pickUpLocation;
    private String dropOffLocation;
}