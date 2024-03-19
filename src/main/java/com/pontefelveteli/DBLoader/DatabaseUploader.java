package com.pontefelveteli.DBLoader;

import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.domain.Role;
import com.pontefelveteli.repository.AddressRepository;
import com.pontefelveteli.repository.AppUserRepository;
import com.pontefelveteli.service.AddressService;
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
    private AddressRepository addressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void uploadData() {
        List<AppUser> users = new ArrayList<>();
        List<Address> addresses = new ArrayList<>();

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
        user2.setRoles(List.of(Role.ROLE_USER));
        user2.setSocialSecurityNumber(123456789);
        user2.setTaxNumber(987654321);
        user2.setEmail("john.doe@example.com");
        user2.setPhoneNumbers(List.of("123456789", "987654321"));
        user2.setPassword(passwordEncoder.encode("password123"));
        Address address1 = new Address();
        address1.setCity("Budapest");
        address1.setUser(user2);
        address1.setZipCode(3213);
        address1.setStreet("Kossuth");
        address1.setHouseNumber("23");
        addresses.add(address1);
        user2.setAddressList(new ArrayList<>(List.of(address1)));
        users.add(user2);


        AppUser user3 = new AppUser();
        user3.setName("John Dolly");
        user3.setDateOfBirth(LocalDate.of(1994, 5, 15));
        user3.setNameOfMother("Emily Doe");
        user3.setRoles(List.of(Role.ROLE_USER));
        user3.setSocialSecurityNumber(1456789);
        user3.setTaxNumber(61787161);
        user3.setEmail("john.Dolly@example.com");
        user3.setPhoneNumbers(List.of("06201234567", "06201234467"));
        user3.setPassword(passwordEncoder.encode("password321"));
        Address address2 = new Address();
        address2.setCity("Szeged");
        address2.setUser(user3);
        address2.setZipCode(6700);
        address2.setStreet("Ady");
        address2.setHouseNumber("12");
        addresses.add(address2);
        user3.setAddressList(new ArrayList<>(List.of(address2)));
        users.add(user3);

        // Adatok feltöltése az adatbázisba
        appUserRepository.saveAll(users);
        addressRepository.saveAll(addresses);
    }
}
