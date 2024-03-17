package com.pontefelveteli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppUserCommand {

    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String nameOfMother;

    private Integer zipCode;

    private String city;

    private String street;

    private String houseNumber;

    private Integer socialSecurityNumber;

    private Integer taxNumber;

    private String email;

    private List<String> phone_numbers;
}
