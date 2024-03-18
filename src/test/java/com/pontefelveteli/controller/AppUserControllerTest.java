package com.pontefelveteli.controller;

import com.pontefelveteli.dto.AppUserInfo;

import com.pontefelveteli.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


import static com.pontefelveteli.domain.Role.ROLE_ADMIN;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = AppUserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AppUserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SecurityContext securityContextMock;
    @MockBean
    private Authentication authenticationMock;

    @MockBean
    private AppUserService appUserService;

    private AppUserInfo appUserInfo;
    private AppUserInfo appUserInfo2;

    @BeforeEach
    void test_init() {
        appUserInfo = AppUserInfo.builder().name("John Doe").build();
        appUserInfo2 = AppUserInfo.builder().name("Alice Doe").build();
    }


    @Test
    void getAllAppUsersTest() throws Exception {
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);
        SecurityContextHolder.setContext(securityContextMock);
        when(authenticationMock.getPrincipal()).thenReturn("testUser");
        UserDetails userDetails = new User("testUser", "password", new ArrayList<>(ROLE_ADMIN.ordinal()));
        when(appUserService.loadUserByUsername("testUser")).thenReturn(userDetails);



        AppUserInfo appUserInfoList
        when(appUserService.listAllAppUsers(1, 2)).thenReturn(appUserInfoList);

        ResultActions response = mockMvc.perform(get("/api/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .param("pageNo","1")
                .param("pageSize", "2"));

        response.andExpect(MockMvcResultMatchers.status().isOk());
//                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()", CoreMatchers.is(appUserInfoList.size())));
    }
}
