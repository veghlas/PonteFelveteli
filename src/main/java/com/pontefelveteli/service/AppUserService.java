package com.pontefelveteli.service;


import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.AddressInfo;
import com.pontefelveteli.dto.AppUserinfo;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AppUserService {
    private AppUserRepository appUserRepository;
    private AddressService addressService;
    private ModelMapper modelMapper;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AddressService addressService, ModelMapper modelMapper) {
        this.appUserRepository = appUserRepository;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
    }

    public AppUserinfo saveAppUser(CreateAppUserCommand createAppUserCommand) {
        AppUser appUser = modelMapper.map(createAppUserCommand, AppUser.class);
        List<AddressInfo> addressInfoList = addressService.saveAddress(appUser, createAppUserCommand.getCreateAddressCommandList());
        appUserRepository.save(appUser);
        AppUserinfo appUserinfo = modelMapper.map(appUser, AppUserinfo.class);
        appUserinfo.setAddressInfoList(addressInfoList);
        return appUserinfo;
    }

}
