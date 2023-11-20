package com.decagon.karrigobe.payload.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @Size(min = 3, max = 25, message = "First name is too long or short")
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @Size(max = 25, message = "Last name is too long")
    private String lastName;

    @Size(max = 35)
    @Email
    @NotBlank(message = "Email should not be blank!")
    private String email;

    @Size(min = 4, max = 15, message = "Password too short or long")
    @NotBlank(message = "Password cannot be blank!")
    private String password;

    @Size(min = 4, max = 15, message = "Password too short or long")
    @NotBlank(message = "Password cannot be blank!")
    private String confirmPassword;

    @Size(min = 11, max = 11, message = "Phone number too long or short")
    @NotBlank
    private String phoneNumber;

    @Size(min = 3, max = 100, message = "Address is too long or short")
    @NotBlank(message = "Address cannot be empty!")
    private  String address;

    @NotNull(message = "Gender cannot be empty")
    private String gender;

}