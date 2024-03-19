package com.pontefelveteli.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pontefelveteli.domain.AppUser;
import com.pontefelveteli.dto.AuthenticationRequestDto;
import com.pontefelveteli.dto.AuthenticationResponseDto;
import com.pontefelveteli.dto.CreateAppUserCommand;
import com.pontefelveteli.repository.AppUserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AppUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired

    private AppUserRepository appUserRepository;


    @Test
    @WithMockUser(roles = "ADMIN")
    public void saveAppUser_Success() throws Exception {
        // Arrange
        CreateAppUserCommand createAppUserCommand = new CreateAppUserCommand();
        createAppUserCommand.setName("John");
        createAppUserCommand.setPassword("password");

        // Act & Assert
        mockMvc.perform(post("/api/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createAppUserCommand)))
                .andExpect(status().isCreated())
                .andExpect(content().string("User has been created."));

        // Assert that the user was saved in the database
        AppUser savedUser = appUserRepository.findByName("John Doe").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
    }


}
