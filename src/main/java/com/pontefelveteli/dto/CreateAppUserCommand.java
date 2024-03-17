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

    private List<CreateAddressCommand> createAddressCommandList;

    private Integer socialSecurityNumber;

    private Integer taxNumber;

    private String email;

    private String password;

    private List<String> phone_numbers;
}
