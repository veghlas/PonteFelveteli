package com.pontefelveteli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppUserCommand {
    private String firstName;

    private String lastName;

    private LocalDate dateOfBirth;

    private String nameOfMother;

    private UpdateAddressCommand updateAddressCommand;

    private Integer socialSecurityNumber;

    private Integer taxNumber;

    private String email;

    private List<String> phone_numbers;
}
