package com.pontefelveteli.controller;

import com.pontefelveteli.dto.AppUserinfo;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.dto.UpdateAppUserCommand;
import com.pontefelveteli.dto.UserMailToDelete;
import com.pontefelveteli.service.AppUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.OutputKeys;
import java.util.List;


@RestController
@RequestMapping("/api/users")
@Slf4j
public class AppUserController {
    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/save")
    public ResponseEntity<AppUserinfo> saveAppUser(@RequestBody @Valid CreateAppUserCommand createAppUserCommand) {
        log.info("Http request, POST / /api/users, with command: " + createAppUserCommand.toString());
        AppUserinfo appUserInfo = appUserService.saveAppUser(createAppUserCommand);
        return new ResponseEntity<>(appUserInfo, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AppUserinfo>> getAllAppUsers(@RequestParam(value = "pageNo", defaultValue = "0", required = false) Integer pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "10", required = false) Integer pageSize) {
        log.info("Http request, GET / /api/users");
        List<AppUserinfo> appUserinfoList = appUserService.listAllAppUsers(pageNo, pageSize);
        return new ResponseEntity<>(appUserinfoList, HttpStatus.OK);
    }

    @PutMapping("/update-data")
    public ResponseEntity<AppUserinfo> updateAppUser(@RequestBody @Valid UpdateAppUserCommand updateAppUserCommand) {
        log.info("Http request, PUT / /api/users/update-data, with command " + updateAppUserCommand.toString());
        AppUserinfo appUserinfo = appUserService.updateAppUser(updateAppUserCommand);
        return new ResponseEntity<>(appUserinfo, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAppUser(@RequestBody UserMailToDelete deleteCommand){
        log.info("Http request, DELETE / /api/users/delete");
        appUserService.deleteAppUser(deleteCommand);
        return new ResponseEntity<>("User delete was successful", HttpStatus.OK);

    }

}
