package com.skincarean.skincarean;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skincarean.skincarean.entity.User;
import com.skincarean.skincarean.model.user.response.TokenResponse;
import com.skincarean.skincarean.model.user.response.UserResponse;
import com.skincarean.skincarean.model.user.response.WebResponse;
import com.skincarean.skincarean.repository.UserRepository;
import com.skincarean.skincarean.utils.SimpleDisplayNameGenerator;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.Token;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.print.attribute.standard.Media;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(SimpleDisplayNameGenerator.class)
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUnAuthorizedUser() throws Exception {
        mockMvc.perform(get("/api/users/current-user").accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", "notfound")).andExpectAll(status().isUnauthorized()).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<String>>() {
            });
            Assertions.assertEquals("Unauthorized", response.getErrors());
        });
    }

    @Test
    void getCurrentUserSuccess() throws Exception {

        Optional<User> user = userRepository.findById("muhammadfiqri");
        String token = user.get().getToken();
        mockMvc.perform(get("/api/users/current-user").accept(MediaType.APPLICATION_JSON).header("X-API-TOKEN", token)).andExpectAll(status().isOk()).andDo(result -> {
            WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<UserResponse>>() {
            });

            Assertions.assertNull(response.getErrors());

        });
    }
}
