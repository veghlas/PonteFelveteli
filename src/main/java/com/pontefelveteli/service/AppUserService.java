package com.pontefelveteli.service;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pontefelveteli.config.SecretConfig;
import com.pontefelveteli.domain.Address;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.domain.Role;
import com.pontefelveteli.dto.*;
import com.pontefelveteli.repository.AppUserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppUserService implements UserDetailsService {
    private AppUserRepository appUserRepository;
    private AddressService addressService;
    private ModelMapper modelMapper;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;

    @Value("${security.jwt.expiration}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${security.jwt.refresh}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    @Autowired
    public AppUserService(AppUserRepository appUserRepository, AddressService addressService, ModelMapper modelMapper, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.addressService = addressService;
        this.modelMapper = modelMapper;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveAppUser(CreateAppUserCommand createAppUserCommand) {
        AppUser appUser = modelMapper.map(createAppUserCommand, AppUser.class);
        appUser.setPassword(passwordEncoder.encode(createAppUserCommand.getPassword()));
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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = findByName(username);
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.name())));
        return User.withUsername(appUser.getName())
                .password(appUser.getPassword())
                .authorities(authorities)
                .build();
    }

    private List<Role> parseRoles(UserDetails user) {
        return user.getAuthorities()
                .stream()
                .map(authority -> Role.valueOf(authority.getAuthority()))
                .collect(Collectors.toList());
    }

    public AuthenticationResponseDto login(AuthenticationRequestDto authenticationRequestDto) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationRequestDto.getName(), authenticationRequestDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256(SecretConfig.getSecret().getBytes());
        String accessToken = JWT.create()
                // Unique data about user
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                // Company name or company url
                .withIssuer("http://localhost.com:8080/api/users/login")
                // Roles
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken = JWT.create()
                // Unique data about user
                .withSubject(user.getUsername())
                // Hosszabb idő kell, bármennyi lehet
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                // Company name or company url
                .withIssuer("http://localhost:8080/api/users/login")
                .sign(algorithm);
        return new AuthenticationResponseDto(accessToken, refreshToken, createAuthenticatedUserInfo(authenticationRequestDto));
    }

    private AuthenticatedUserInfo createAuthenticatedUserInfo(AuthenticationRequestDto authenticationRequestDto) {
        AuthenticatedUserInfo authenticatedUserInfo = new AuthenticatedUserInfo();
        authenticatedUserInfo.setEmail(authenticationRequestDto.getName());
        authenticatedUserInfo.setRoles(parseRoles(loadUserByUsername(authenticationRequestDto.getName())));
        return authenticatedUserInfo;
    }


    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        Algorithm algorithm = Algorithm.HMAC256(SecretConfig.getSecret().getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
        String username = decodedJWT.getSubject();
        AppUser user = findByName(username);

        String accessToken = JWT.create()
                .withSubject(user.getEmail())
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .withIssuer("http://localhost:8080/api/users/token/refresh")
                .withClaim("roles", user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()))
                .sign(algorithm);

        return new RefreshTokenResponse(accessToken);
    }
}
