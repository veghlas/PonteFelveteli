package com.pontefelveteli.service;


import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.AddressInfo;
import com.pontefelveteli.dto.AppUserinfo;
import com.pontefelveteli.dto.CreateAppUserCommand;
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
            AppUserinfo appUserinfo = modelMapper.map(appUser, AppUserinfo.class);
            appUserinfo.setAddressInfoList(addressService.mapAddresListToAddresInfoList(appUser.getAddressList()));
            appUserinfoList.add(appUserinfo);
        });
        return appUserinfoList;
    }
}
