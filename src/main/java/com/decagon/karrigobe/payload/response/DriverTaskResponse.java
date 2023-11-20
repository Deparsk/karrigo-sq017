package com.decagon.karrigobe.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverTaskResponse implements Serializable {
    Long TaskId;
    List<OrderResponse> orderLocation;
}