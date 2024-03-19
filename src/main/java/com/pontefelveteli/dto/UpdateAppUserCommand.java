package com.pontefelveteli.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppUserCommand {

    @Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ ]{2,20}",
            message = "Name must be 2 to 20 characters without numeric digits.")
    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth should be in the past")
    private LocalDate dateOfBirth;

    @Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ ]{2,20}",
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
    private String email;

    private List<String> phone_numbers;
}
