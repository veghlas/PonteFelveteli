package com.pontefelveteli.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppUserCommand {

    @Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}",
            message = "First name must be 2 to 20 characters without numeric digits.")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth should be in the past")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Name of mother is required")
    private String nameOfMother;

    private UpdateAddressCommand updateAddressCommand;

    private Integer socialSecurityNumber;

    private Integer taxNumber;

    private String email;

    private List<String> phone_numbers;
}
