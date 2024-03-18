package com.pontefelveteli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserinfo {

    private String Name;

    private LocalDate dateOfBirth;

    private String nameOfMother;

    private List<AddressInfo> addressInfoList;

    private Integer socialSecurityNumber;

    private Integer taxNumber;

    private String email;

    private List<String> phone_numbers;
}
