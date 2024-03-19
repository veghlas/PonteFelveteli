package com.pontefelveteli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserInfo {

    private String name;

    private LocalDate dateOfBirth;

    private String nameOfMother;

    private List<AddressInfo> addressInfoList;

    private Integer socialSecurityNumber;

    private Integer taxNumber;

    private String email;

    private List<String> phoneNumbers;
}
