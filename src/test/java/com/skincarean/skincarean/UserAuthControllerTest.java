package com.skincarean.skincarean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skincarean.skincarean.model.user.request.LoginUserRequest;
import com.skincarean.skincarean.model.user.request.RegisterUserRequest;
import com.skincarean.skincarean.model.user.response.TokenResponse;
import com.skincarean.skincarean.model.user.response.WebResponse;
import com.skincarean.skincarean.repository.UserRepository;
import com.skincarean.skincarean.utils.SimpleDisplayNameGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(SimpleDisplayNameGenerator.class)
class UserAuthControllerTest {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerFailed() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("");
        request.setPassword("");
        request.setEmail("");
        request.setAddress("");
        request.setPhone("");
        request.setFullName("");
        request.setPhotoProfile("");
        request.setConfirmPassword("");

        mockMvc.perform(post("/api/users/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isBadRequest()).andDo(result -> {
                    WebResponse<String> o = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
                    });
                    Assertions.assertEquals(false, o.getIsSuccess());
                });


    }

    @Test
    void registerSuccess() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setUsername("muhammadfiqri");
        request.setPassword("fiqri");
        request.setAddress("TCP");
        request.setPhone("03828392");
        request.setEmail("fiqri@gmail.com");
        request.setFullName("Muhammad Fiqri Turham");
        request.setConfirmPassword("fiqri");
        mockMvc.perform(post("/api/users/auth/register")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(status().isOk()).andDo(result -> {
            WebResponse<String> o = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            Assertions.assertEquals("Register berhasil", o.getData());

        });


    }

    @Test
    void loginFailedUserNotFound() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("askdosakdo");
        request.setPassword("kajdssad");
        mockMvc
                .perform(post("/api/users/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isUnauthorized()).andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void loginSuccess() throws Exception {
        LoginUserRequest request = new LoginUserRequest();
        request.setUsername("muhammadfiqri");
        request.setPassword("fiqri");

        mockMvc.perform(post("/api/users/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpectAll(status().isOk())
                .andDo(result -> {
                    WebResponse<TokenResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
                    });

                    Assertions.assertNotNull(response.getData());
                });
    }

}
