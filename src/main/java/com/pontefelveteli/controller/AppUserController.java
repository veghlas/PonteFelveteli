package com.pontefelveteli.controller;

import com.pontefelveteli.dto.AppUserinfo;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.service.AppUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class AppUserController {
    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping
    public ResponseEntity<AppUserinfo> saveAppUser(@RequestBody @Valid CreateAppUserCommand createAppUserCommand) {
        log.info("Http request, POST / /api/users, with command: " + createAppUserCommand.toString());
        AppUserinfo appUserInfo = appUserService.saveAppUser(createAppUserCommand);
        return new ResponseEntity<>(appUserInfo, HttpStatus.CREATED);
    }
}
