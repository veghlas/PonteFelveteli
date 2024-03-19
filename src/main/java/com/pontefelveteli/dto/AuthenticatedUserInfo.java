package com.pontefelveteli.dto;

import com.pontefelveteli.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticatedUserInfo {
    private String name;
    private List<Role> roles;
}
