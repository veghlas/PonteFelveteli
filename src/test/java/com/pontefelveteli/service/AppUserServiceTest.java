package com.pontefelveteli.service;

import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.domain.Role;
import com.pontefelveteli.dto.*;
import com.pontefelveteli.exception.UserNotFoundById;
import com.pontefelveteli.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
    private AppUserRepository appUserRepositoryMock;
    @Mock
    private AddressService addressService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDetails userDetailsMock;
    @Mock
    private Authentication authenticationMock;
    @Mock
    private SecurityContext securityContextMock;

    private AppUserService appUserService;

    @BeforeEach
    void init() {
        appUserRepositoryMock = mock(AppUserRepository.class);
        appUserService = new AppUserService(appUserRepositoryMock,
                addressService,
                modelMapper,
                authenticationManager,
                passwordEncoder);
    }

    @Test
    void saveAppUserTest() {

        AppUser appUser = new AppUser();
        CreateAppUserCommand createAppUserCommand = new CreateAppUserCommand("John Doe", "alma");

        // modelMapper.map() hívás mock-olása, amely az AppUser objektumot készíti el a CreateAppUserCommand objektum alapján
        when(modelMapper.map(createAppUserCommand, AppUser.class)).thenReturn(appUser);

        // passwordEncoder.encode() hívás mock-olása, amely az AppUser jelszavát kódolja
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("test1234");

        // ArgumentCaptor létrehozása az AppUser objektum fogadására
        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);

        // saveAppUser metódus meghívása
        appUserService.saveAppUser(createAppUserCommand);

        // Ellenőrizzük, hogy a save() metódus meghívódott-e az appUserRepositoryMock-on, és el lett kapva az AppUser objektum
        verify(appUserRepositoryMock).save(appUserArgumentCaptor.capture());

        // Elkapjuk az AppUsert
        AppUser result = appUserArgumentCaptor.getValue();

        assertEquals("test1234", result.getPassword());
        assertEquals(1, result.getRoles().size());
    }


    @Test
    void listAllAppUsersTest() {

        //Pageable létrehozása
        Pageable pageable = PageRequest.of(1, 10);
        AppUser appUser = new AppUser();
        appUser.setName("John Doe");
        List<AppUser> appUserList = new ArrayList<>();
        appUserList.add(appUser);
        Page<AppUser> appUserPage = new PageImpl<>(appUserList, pageable, 1);
        when(appUserRepositoryMock.findAll(pageable)).thenReturn(appUserPage);

        //Új AppuserInfo a modelMapper mockoláshoz
        AppUserInfo expectedUserInfo = new AppUserInfo();
        expectedUserInfo.setName("John Doe");
        when(modelMapper.map(appUser, AppUserInfo.class)).thenReturn(expectedUserInfo);
        // Tesztelt metódus meghívása
        List<AppUserInfo> appUserInfoList = appUserService.listAllAppUsers(1, 10);

        // Ellenőrzések
        assertEquals(1, appUserInfoList.size());

        // Ellenőrizzük, hogy a repository findAll() metódusa pontosan egyszer lett meghívva a megfelelő oldalszámmal és oldalmérettel
        verify(appUserRepositoryMock, times(1)).findAll(pageable);
    }

    @Test
    void updateAppUserTest() {
        UpdateAddressCommand updateAddressCommand = new UpdateAddressCommand();
        updateAddressCommand.setCity("Budapest");
        updateAddressCommand.setZipCode(2331);
        updateAddressCommand.setStreet("Kossuth");
        updateAddressCommand.setHouseNumber("2/A");

        UpdateAppUserCommand updateAppUserCommand = new UpdateAppUserCommand();
        updateAppUserCommand.setEmail("example@gmail.com");
        updateAppUserCommand.setName("Béla");
        updateAppUserCommand.setDateOfBirth(LocalDate.of(1990, 01, 01));
        updateAppUserCommand.setPhone_numbers(new ArrayList<>());
        updateAppUserCommand.setNameOfMother("Mother");
        updateAppUserCommand.setTaxNumber(12321);
        updateAppUserCommand.setSocialSecurityNumber(12323);
        updateAppUserCommand.setSocialSecurityNumber(12323);
        updateAppUserCommand.setUpdateAddressCommand(updateAddressCommand);

        String username = "testuser";
        AppUser userToUpdate = new AppUser();
        userToUpdate.setName(username);
        when(userDetailsMock.getUsername()).thenReturn(username);
        when(appUserRepositoryMock.findByName(username)).thenReturn(Optional.of(userToUpdate));

        Address address = new Address();
        address.setCity("Budapest");
        List<Address> addressList = new ArrayList<>(List.of(address));
        when(addressService.updateAddress(any(AppUser.class), any(UpdateAddressCommand.class))).thenReturn(addressList);


        // Tesztelt metódus meghívása
        AppUserInfo updatedUserInfo = appUserService.updateAppUser(mock(UserDetails.class), updateAppUserCommand);

        assertEquals("email@email.com", updatedUserInfo.getEmail());

        // Ellenőrizzük, hogy a megfelelő függvényeket hívták-e meg a megfelelő paraméterekkel
        verify(appUserRepositoryMock).findByName(any(String.class));
        verify(modelMapper).map(updateAppUserCommand, AppUser.class);
        verify(addressService).updateAddress(any(AppUser.class), any(UpdateAddressCommand.class));
    }

    private AppUser getAndLoginWithAppUser() {
        AppUser appUser = new AppUser();
        appUser.setName("John Doe");
                appUser.setPassword(passwordEncoder.encode("password"));
        appUser.setRoles(List.of(Role.ROLE_USER));

        TestingAuthenticationToken authentication =
                new TestingAuthenticationToken(appUser.getName(), appUser.getPassword(), appUser.getRoles().get(0).toString());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return appUser;
    }

    @Test
    void deleteMyUser_UserExists_DeleteSuccessful() {

        String username = "testuser";
        AppUser userToDelete = new AppUser();
        userToDelete.setName(username);
        when(userDetailsMock.getUsername()).thenReturn(username);
        when(appUserRepositoryMock.findByName(username)).thenReturn(Optional.of(userToDelete));

        appUserService.deleteMyUser(userDetailsMock);


        verify(appUserRepositoryMock, times(1)).delete(userToDelete);
    }

    @Test
    void deleteMyUser_UserNotFound_ThrowException() {

        String username = "nonexistentuser";
        when(userDetailsMock.getUsername()).thenReturn(username);
        when(appUserRepositoryMock.findByName(username)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.deleteMyUser(userDetailsMock);
        });
    }

    @Test
    void findById_UserExists_ReturnUser() {
        Integer userId = 1;
        AppUser user = new AppUser();
        user.setId(userId);
        when(appUserRepositoryMock.findById(userId)).thenReturn(Optional.of(user));

        AppUser result = appUserService.findById(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
    }

    @Test
    void findById_UserNotFound_ThrowException() {
        Integer userId = 2;
        when(appUserRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundById.class, () -> {
            appUserService.findById(userId);
        });
    }


    @Test
    void updatePasswordTest() throws Exception {
        // Given
        AppUser appUser = new AppUser();
        appUser.setName("loggedInUser");
        appUser.setPassword("oldPassword");


        // Set up the update password command
        UpdatePasswordCommand command = new UpdatePasswordCommand();
        command.setOldPassword("oldPassword");
        command.setNewPassword("newPassword");

        // Create UserDetails object
        UserDetails userDetails = User.builder()
                .username("loggedInUser")
                .password("oldPassword")
                .authorities(Collections.emptyList())
                .build();

        // Mock the behavior of appUserRepository
        when(appUserRepositoryMock.findByName(userDetails.getUsername())).thenReturn(Optional.of(appUser));
        // When
        String result = appUserService.updatePassword(userDetails, command);

        // Then
        assertEquals("Your old password is wrong!", result);
        assertEquals("oldPassword", appUser.getPassword());
    }


}

