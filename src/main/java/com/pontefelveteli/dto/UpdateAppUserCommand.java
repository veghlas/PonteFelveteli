package com.pontefelveteli.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppUserCommand {

    @Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}",
            message = "Name must be 2 to 20 characters without numeric digits.")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth should be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}",
            message = "Name of mother must be 2 to 20 characters without numeric digits.")
    @NotBlank(message = "Name is required")
    private String nameOfMother;

    private UpdateAddressCommand updateAddressCommand;

    @NotNull(message = "Cannot be null!")
    private Integer socialSecurityNumber;

    @NotNull(message = "Cannot be null!")
    private Integer taxNumber;

    @Pattern(regexp = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",
            message = "Incorrect email format!")
    @NotBlank(message = "Cannot be blank!")
    private String email;

    @NotBlank(message = "Phone number is required!")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$", message = "Incorrect phone number format!")
    private List<String> phone_numbers;
}
