package com.pontefelveteli.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.repository.AppUserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.pontefelveteli.domain.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AppUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired

    private AppUserRepository appUserRepository;
    private AppUser appUser1;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void test_init() {


        appUser1 = new AppUser();
        appUser1.setName("John");
        appUser1.setEmail("user@email.com");
        appUser1.setRoles(new ArrayList<>(List.of(ROLE_USER)));
        appUser1.setPassword(passwordEncoder.encode("Password"));
        entityManager.persist(appUser1);
    }

    @Test
    void loginTest() throws Exception {
        String basicProductJsonData = "{\n" +
                "\"name\": \"John\",\n" +
                "\"password\": \"Password\"\n" +
                "}";

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(basicProductJsonData))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty())
                .andExpect(jsonPath("$.authenticatedUserInfo.name").value("John"))
                .andExpect(jsonPath("$.authenticatedUserInfo.roles").value("ROLE_USER"));
    }

}
