package com.pontefelveteli.service;


import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.*;
import com.pontefelveteli.exception.UsernameNotFoundException;
import com.pontefelveteli.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public void saveAppUser(CreateAppUserCommand createAppUserCommand) {
        AppUser appUser = modelMapper.map(createAppUserCommand, AppUser.class);
        appUserRepository.save(appUser);
    }

    public List<AppUserinfo> listAllAppUsers(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<AppUser> appUserPage = appUserRepository.findAll(pageable);
        List<AppUser> appUserList = appUserPage.getContent();
        List<AppUserinfo> appUserinfoList = new ArrayList<>();
        appUserList.forEach(appUser -> {
            AppUserinfo appUserinfo = getAppUserinfo(appUser);
            appUserinfoList.add(appUserinfo);
        });
        return appUserinfoList;
    }

    public AppUserinfo updateAppUser(UpdateAppUserCommand updateAppUserCommand) {
        AppUser appUserToUpdate = findByName(updateAppUserCommand.getName());
        modelMapper.map(updateAppUserCommand, appUserToUpdate);
        List<Address> addressList = addressService.updateAddress(appUserToUpdate, updateAppUserCommand.getUpdateAddressCommand());
        appUserToUpdate.setAddressList(addressList);
        appUserRepository.save(appUserToUpdate);
        return getAppUserinfo(appUserToUpdate);
    }

    private AppUserinfo getAppUserinfo(AppUser appUserToUpdate) {
        AppUserinfo appUserinfo = modelMapper.map(appUserToUpdate, AppUserinfo.class);
        appUserinfo.setAddressInfoList(addressService.mapAddresListToAddresInfoList(appUserToUpdate.getAddressList()));
        return appUserinfo;
    }

    public AppUser findByName(String name) {
        Optional<AppUser> appUserOptional = appUserRepository.findByName(name);
        if (appUserOptional.isEmpty()) {
            throw new UsernameNotFoundException(name);
        }
        return appUserOptional.get();
    }

    public void deleteAppUser(UserNameToDelete deleteCommand) {
        appUserRepository.delete(findByName(deleteCommand.getName()));
    }

//    public AppUserinfo updatePassword(UpdatePasswordCommand updatePasswordCommand) {
//
//    }
}
