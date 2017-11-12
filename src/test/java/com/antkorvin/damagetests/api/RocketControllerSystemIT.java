package com.antkorvin.damagetests.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * Rocket REST API system test.
 * (Testing: Database & MVC & service integration)
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RocketControllerSystemIT {

    @Autowired
    protected MockMvc mockMvc;

    /**
     * Test for create new rocket
     */
    @Test
    public void createTest() throws Exception {
        // Act
        mockMvc.perform(post("/{url}/create", RocketController.URL).param("name", "booom"))
               // Asserts
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(jsonPath("$.id", notNullValue()))
               .andExpect(jsonPath("$.name", notNullValue()))
               .andExpect(jsonPath("$.launchCode", notNullValue()));
    }

    /**
     * Check get existing rocket from DB
     */
    @Test
    public void getTest() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        // Act
        mockMvc.perform(get("/{url}/{id}", RocketController.URL, id))
               .andDo(print())
               .andExpect(status().isOk());

        // Asserts
        //TODO: check this case!
    }
}
