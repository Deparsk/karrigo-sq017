package com.decagon.karrigobe.payload.request;

import com.decagon.karrigobe.entities.enums.ItemCategory;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDescriptionRequest {
    @NotBlank(message = "Item name is required.")
    @Size(min = 2, max = 100)
    private String itemName;

    @NotBlank(message = "Item Description is required.")
    @Size(min = 10, max = 1000)
    private String itemDescription;

    private Double itemWeight;

    @NotNull(message = "Item cost price must be declared.")
    private Double declaredPrice;

    @NotBlank(message = "Item category is required.")
    @Size(min = 3, max = 20)
    private String itemCategory;

    @NotBlank(message = "Pickup location is required.")
    @Size(min = 2, max = 500)
    private String pickUpLocation;

    @NotBlank(message = "Drop-off location is required.")
    @Size(min = 2, max = 500)
    private String dropOffLocation;

    @NotNull(message = "Distance should not be blank.")
    private Double distance;
}
