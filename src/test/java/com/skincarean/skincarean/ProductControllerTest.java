package com.skincarean.skincarean;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skincarean.skincarean.model.product.response.DetailProductResponse;
import com.skincarean.skincarean.model.product.response.ProductResponse;
import com.skincarean.skincarean.model.user.response.WebResponse;
import com.skincarean.skincarean.repository.ProductRepository;
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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(SimpleDisplayNameGenerator.class)
public class ProductControllerTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllProductTest() throws Exception {
        mockMvc.perform(get("/api/products").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
            WebResponse<List<ProductResponse>> o = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<List<ProductResponse>>>() {
            });

            Assertions.assertNotNull(o.getData());
        });
    }


    @Test
    void getDetailProductNotFoundTest() throws Exception {


        mockMvc.perform(get("/api/products/20").accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isNotFound()).andDo(result -> {
            WebResponse<DetailProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<DetailProductResponse>>() {
            });

            Assertions.assertNull(response.getData());
        });
    }

    @Test
    void getDetailProductTest() throws Exception {

        String productId = "0297fec5-942b-4a8b-a80d-d7ffbfdea750";
        mockMvc.perform(get("/api/products/" + productId).accept(MediaType.APPLICATION_JSON)).andExpectAll(status().isOk()).andDo(result -> {
            WebResponse<DetailProductResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<WebResponse<DetailProductResponse>>() {
            });

            Assertions.assertNotNull(response.getData());
        });
    }
}
