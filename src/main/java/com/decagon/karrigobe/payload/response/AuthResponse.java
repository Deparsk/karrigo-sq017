package com.decagon.karrigobe.payload.response;

import lombok.*;

import java.time.LocalDateTime;

import static com.decagon.karrigobe.utils.DateUtils.toDateString;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String dateTime;

    public AuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.dateTime = toDateString(LocalDateTime.now());
    }
}
