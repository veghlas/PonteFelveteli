package com.pontefelveteli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAddressCommand {
    private Integer id;

    private Integer zipCode;

    private String city;

    private String street;

    private String houseNumber;
}
