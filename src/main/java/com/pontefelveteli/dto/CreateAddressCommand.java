package com.pontefelveteli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressCommand {
    private Integer zipCode;

    private String city;

    private String street;

    private String houseNumber;
}
