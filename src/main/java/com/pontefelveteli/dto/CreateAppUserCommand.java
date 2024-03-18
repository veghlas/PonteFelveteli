package com.pontefelveteli.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppUserCommand {

    @Pattern(regexp = "^[A-Za-záéiíoóöőuúüűÁÉIÍOÓÖŐUÚÜŰ]{2,20}",
            message = "Name must be 2 to 20 characters without numeric digits.")
    @NotBlank(message = "Name is required")
    private String name;

    private String password;
}
