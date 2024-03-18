package com.pontefelveteli.controller;

import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.*;
import com.pontefelveteli.service.AppUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.pontefelveteli.config.SecurityConfiguration.extractToken;
import static com.pontefelveteli.config.SecurityConfiguration.invalidateToken;


@RestController
@RequestMapping("/api/users")
@Slf4j
public class AppUserController {
    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/create")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<String> saveAppUser(@RequestBody @Valid CreateAppUserCommand createAppUserCommand) {
        log.info("Http request, POST / /api/users/create, with command: " + createAppUserCommand.toString());
        appUserService.saveAppUser(createAppUserCommand);
        return new ResponseEntity<>("User has been created.", HttpStatus.CREATED);
    }

    @GetMapping
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<AppUserinfo>> getAllAppUsers(@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.info("Http request, GET / /api/users");
        List<AppUserinfo> appUserinfoList = appUserService.listAllAppUsers(pageNo, pageSize);
        return new ResponseEntity<>(appUserinfoList, HttpStatus.OK);
    }

    @PutMapping("/update-data")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<AppUserinfo> updateAppUser(@RequestBody @Valid UpdateAppUserCommand updateAppUserCommand) {
        log.info("Http request, PUT / /api/users/update-data, with command " + updateAppUserCommand.toString());
        AppUserinfo appUserinfo = appUserService.updateAppUser(updateAppUserCommand);
        return new ResponseEntity<>(appUserinfo, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> deleteAppUser(@RequestBody UserNameToDelete deleteCommand) {
        log.info("Http request, DELETE / /api/users/delete");
        appUserService.deleteAppUser(deleteCommand);
        return new ResponseEntity<>("User delete was successful", HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@RequestBody AuthenticationRequestDto authenticationRequestDto) {
        AuthenticationResponseDto responseDto = appUserService.login(authenticationRequestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = extractToken(request);
        invalidateToken(token);
        return new ResponseEntity<>("You have logged out.", HttpStatus.OK);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        RefreshTokenResponse tokenResponse = appUserService.refreshToken(refreshTokenRequest);
        return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @PutMapping("/update-password")
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    public ResponseEntity<String> changePassword(@RequestBody @Valid UpdatePasswordCommand command) {
        log.info("Http request, PUT / /users/update-password with command: " + command.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails loggedInUser = appUserService.loadUserByUsername((String) authentication.getPrincipal());
        String resultMessage = appUserService.updatePassword(loggedInUser, command);
        return new ResponseEntity<>(resultMessage,
                resultMessage.equals("Password changed successfully!") ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

}
