package com.pontefelveteli.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressCommand {
    private Integer id;

    @Min(value = 1000, message = "ZipCode is 4 characters")
    @Max(value = 9999, message = "ZipCode is 4 characters")
    @NotNull(message = "ZipCode is required!")
    private Integer zipCode;

    @Size(max = 25, message = "Maximum length of a city name is 25 characters!")
    @NotNull(message = "City is required!")
    private String city;

    @Size(max = 25, message = "Maximum length of a city name is 25 characters!")
    @NotNull(message = "Street is required!")
    private String street;

    @Min(value = 1, message = "House number is a positive number!")
    @NotNull(message = "ZipCode is required!")
    private String houseNumber;
}
