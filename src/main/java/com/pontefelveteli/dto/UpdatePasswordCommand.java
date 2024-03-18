package com.pontefelveteli.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class UpdatePasswordCommand {
    private String oldPassword;
    private String newPassword;
}
