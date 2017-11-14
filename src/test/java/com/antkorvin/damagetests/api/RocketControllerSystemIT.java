package com.antkorvin.damagetests.api;

import com.antkorvin.damagetests.api.dto.RocketDTO;
import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.utils.BaseSystemIT;
import com.github.database.rider.core.api.dataset.DataSet;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.http.MediaType;

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
 * <p>
 * Rocket REST API system test. (Testing: Database & MVC & service integration)
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketControllerSystemIT extends BaseSystemIT {


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
    @DataSet(cleanBefore = true, value = "datasets/rockets.json")
    public void getTest() throws Exception {
        // Arrange
        UUID id = UUID.fromString("f26b93f8-a0ab-496c-8b24-3b446cb1c50c");
        // Act
        String content = mockMvc.perform(get("/{url}/{id}", RocketController.URL, id))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

        // Asserts
        RocketDTO rocket = mapper.readValue(content, RocketDTO.class);
        Assertions.assertThat(rocket)
                  .extracting(RocketDTO::getId,
                              RocketDTO::getName,
                              RocketDTO::getLaunchCode)
                  .contains(id,
                            "Nuclear",
                            "936");
    }
}
