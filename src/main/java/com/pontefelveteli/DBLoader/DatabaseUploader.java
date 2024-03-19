package com.pontefelveteli.DBLoader;

import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.domain.Role;
import com.pontefelveteli.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseUploader {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void uploadData() {
        List<AppUser> users = new ArrayList<>();

        // Adatok létrehozása és hozzáadása a listához
        AppUser user1 = new AppUser();
        user1.setName("Admin");
        user1.setPassword(passwordEncoder.encode("Admin"));
        user1.setRoles(List.of(Role.ROLE_ADMIN));
        users.add(user1);

        // Adatok létrehozása és hozzáadása a listához
        AppUser user2 = new AppUser();
        user2.setName("John Doe");
        user2.setDateOfBirth(LocalDate.of(1990, 5, 15));
        user2.setNameOfMother("Jane Doe");
        user1.setRoles(List.of(Role.ROLE_USER));
        user2.setSocialSecurityNumber(123456789);
        user2.setTaxNumber(987654321);
        user2.setEmail("john.doe@example.com");
        user2.setPhoneNumbers(List.of("123456789", "987654321"));
        Address address1 = new Address();
        address1.setCity("Budapest");
        address1.setUser(user2);
        address1.setZipCode(3213);
        address1.setStreet("Kossuth");
        address1.setHouseNumber("23");
        users.add(user2);

        // További felhasználók hozzáadása...

        // Adatok feltöltése az adatbázisba
        appUserRepository.saveAll(users);
    }
}
