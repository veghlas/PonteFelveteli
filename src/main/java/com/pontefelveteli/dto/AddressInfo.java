package com.pontefelveteli.dto;

import com.pontefelveteli.domain.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfo {

    private Integer zipCode;

    private String city;

    private String street;

    private String houseNumber;

    private AppUser user;
}