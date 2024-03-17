package com.pontefelveteli.service;


import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.domain.Role;
import com.pontefelveteli.dto.AddressInfo;
import com.pontefelveteli.dto.AppUserinfo;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppUserService implements UserDetailsService {
    private AppUserRepository appUserRepository;
    private AddressService addressService;
    private ModelMapper modelMapper;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AddressService addressService, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUserinfo saveAppUser(CreateAppUserCommand createAppUserCommand) {
        AppUser appUser = modelMapper.map(createAppUserCommand, AppUser.class);
        appUser.setPassword(passwordEncoder.encode(createAppUserCommand.getPassword()));
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = findByEmail(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        boolean enabled = !appUser.getActive();
        appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return User.withUsername(appUser.getEmail())
                .password(appUser.getPassword())
                .disabled(enabled)
                .authorities(authorities)
                .build();
    }

    public AppUser findByEmail(String username) {
        Optional<AppUser> appUserOptional = appUserRepository.findByEmail(username);
        if (appUserOptional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        return appUserOptional.get();
    }

    private List<Role> parseRoles(UserDetails user) {
        return user.getAuthorities()
                .stream()
                .map(authority -> Role.valueOf(authority.getAuthority()))
                .collect(Collectors.toList());
    }

}
