package com.skincarean.skincarean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skincarean.skincarean.model.admin.request.RegisterAdminRequest;
import com.skincarean.skincarean.model.user.request.RegisterUserRequest;
import com.skincarean.skincarean.model.user.response.WebResponse;
import com.skincarean.skincarean.repository.AdminRepository;
import com.skincarean.skincarean.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.print.attribute.standard.Media;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerFailed() throws Exception {
        mockMvc.perform(post("/api/admins/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "")
                        .param("password", "")
                        .param("email", "")
                        .param("phone", "")
                        .param("fullName", "")
                        .param("confirmPassword", "")
                        .param("address", "")
                        .param("isAdmin", "")) // "true" karena dikirim sebagai string dalam URL-encoded form
                .andExpectAll(
                        status().isBadRequest(),
                        jsonPath("$.data").value(false)
                )
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });
                    Assertions.assertNotNull(response.getErrors());
                });
    }

    @Test
    void registerSuccess() throws Exception {
        mockMvc.perform(post("/api/admins/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "validUsername")
                        .param("password", "validPassword")
                        .param("email", "test@example.com")
                        .param("phone", "123456789")
                        .param("fullName", "Valid Full Name")
                        .param("confirmPassword", "validPassword")
                        .param("address", "Valid Address")
                        .param("isAdmin", "true")) // "true" karena dikirim sebagai string dalam URL-encoded form
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.data").value("Admin berhasil ditambahkan")
                )
                .andDo(result -> {
                    WebResponse<String> response = objectMapper.readValue(
                            result.getResponse().getContentAsString(),
                            new TypeReference<WebResponse<String>>() {
                            });
                    Assertions.assertEquals("Admin berhasil ditambahkan", response.getData());
                });
    }

}
