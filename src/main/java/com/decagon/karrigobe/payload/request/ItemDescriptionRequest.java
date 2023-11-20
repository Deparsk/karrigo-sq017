package com.decagon.karrigobe.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemDescriptionRequest implements Serializable {
    @Size(min = 2, max = 100, message = "Item name too long or short")
    @NotBlank(message = "Item name cannot be blank")
    private String itemName;

    @Size(min = 3, max = 1000, message = "Item description too long or short")
    @NotBlank(message = "Item description cannot be blank")
    private String itemDescription;

    @Size(min = 1)
    @NotNull(message = "Item weight cannot be blank")
    private Double itemWeight;


    @NotNull(message = "Declared price cannot be blank")
    private Double declaredPrice;
}