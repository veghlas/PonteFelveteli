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

    public AppUserinfo saveAppUser(CreateAppUserCommand createAppUserCommand) {
        AppUser appUser = modelMapper.map(createAppUserCommand, AppUser.class);
        List<AddressInfo> addressInfoList = addressService.saveAddress(appUser, createAppUserCommand.getCreateAddressCommandList());
        appUserRepository.save(appUser);
        AppUserinfo appUserinfo = modelMapper.map(appUser, AppUserinfo.class);
        appUserinfo.setAddressInfoList(addressInfoList);
        return appUserinfo;
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
        AppUser appUserToUpdate = findByEmail(updateAppUserCommand.getEmail());
        modelMapper.map(updateAppUserCommand, appUserToUpdate);
        addressService.updateAddress(appUserToUpdate, updateAppUserCommand.getUpdateAddressCommand());
        appUserRepository.save(appUserToUpdate);
        return getAppUserinfo(appUserToUpdate);
    }

    private AppUserinfo getAppUserinfo(AppUser appUserToUpdate) {
        AppUserinfo appUserinfo = modelMapper.map(appUserToUpdate, AppUserinfo.class);
        appUserinfo.setAddressInfoList(addressService.mapAddresListToAddresInfoList(appUserToUpdate.getAddressList()));
        return appUserinfo;
    }

    public AppUser findByEmail(String email) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(email);
        if (appUserOptional.isEmpty()) {
            throw new UsernameNotFoundException(email);
        }
        return appUserOptional.get();
    }

    public void deleteAppUser(UserMailToDelete deleteCommand) {
        appUserRepository.delete(findByEmail(deleteCommand.getEmail()));
    }
}
