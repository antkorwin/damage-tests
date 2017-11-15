package com.antkorvin.damagetests.api;

import com.antkorvin.damagetests.api.dto.ErrorDTO;
import com.antkorvin.damagetests.api.dto.RocketDTO;
import com.antkorvin.damagetests.exceptions.NotFoundException;
import com.antkorvin.damagetests.mappers.RocketMapperImpl;
import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.services.RocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Korovin Anatolii on 12.11.17.
 * <p>
 * Tests for check MVC layer integration - controller advice - status codes
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@WebMvcTest(RocketController.class)
public class RocketControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RocketService rocketService;

    @SpyBean(name = "rocketMapper")
    private RocketMapperImpl rocketMapper;

    private ObjectMapper mapper = new ObjectMapper();


    /**
     * Check the successful rocket mapping to DTO, after its creation.
     */
    @Test
    public void create() throws Exception {
        // Arrange
        String name = "jelly";
        Rocket rocket = Rocket.builder()
                              .id(UUID.randomUUID())
                              .name("1234")
                              .build();

        when(rocketService.create(eq(name))).thenReturn(rocket);

        // Act
        String content = mockMvc.perform(post("/{url}/create", RocketController.URL)
                                                 .param("name", name))
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn().getResponse().getContentAsString();

        // Asserts
        RocketDTO result = mapper.readValue(content, RocketDTO.class);

        Assertions.assertThat(result)
                  .extracting(RocketDTO::getId,
                              RocketDTO::getName,
                              RocketDTO::getLaunchCode)
                  .contains(rocket.getId(),
                            rocket.getName(),
                            rocket.getLaunchCode());
    }


    @Test
    public void testGet() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        Rocket rocket = Rocket.builder()
                              .id(id)
                              .name("roxy")
                              .launchCode("1234")
                              .build();

        when(rocketService.get(eq(id))).thenReturn(rocket);

        // Act
        String content = mockMvc.perform(get("/{url}/{id}", RocketController.URL, id))
                                .andDo(print())
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

        // Asserts
        RocketDTO result = mapper.readValue(content, RocketDTO.class);

        Assertions.assertThat(result)
                  .isNotNull()
                  .extracting(RocketDTO::getId,
                              RocketDTO::getName,
                              RocketDTO::getLaunchCode)
                  .contains(rocket.getId(),
                            rocket.getName(),
                            rocket.getLaunchCode());
    }

    @Test
    public void testGetWithNotFoundException() throws Exception {
        UUID id = UUID.randomUUID();
        // Arrange
        when(rocketService.get(any(UUID.class)))
                .thenThrow(new NotFoundException("oops", 101010));

        // Act
        String content = mockMvc.perform(get("/{url}/{id}", RocketController.URL, id))
                                .andDo(print())
                                .andExpect(status().isNotFound())
                                .andReturn().getResponse().getContentAsString();

        // Asserts
        ErrorDTO result = mapper.readValue(content, ErrorDTO.class);
        Assertions.assertThat(result)
                  .extracting(ErrorDTO::getMessage,
                              ErrorDTO::getCode)
                  .contains("oops", 101010);
    }

    /**
     * Check handler exception while rocket create, and response InternalServerError code with ErrorDTO to client
     */
    @Test
    public void testThrowExceptionWhileCreate() throws Exception {
        // Arrange
        String msg = "oops";
        when(rocketService.create(any(String.class))).thenThrow(new IndexOutOfBoundsException(msg));

        // Act
        String content = mockMvc.perform(post("/{url}/create", RocketController.URL)
                                                 .param("name", "any"))
                                .andDo(print())
                                .andExpect(status().isInternalServerError())
                                .andReturn().getResponse().getContentAsString();

        // Asserts
        ErrorDTO errorDTO = mapper.readValue(content, ErrorDTO.class);
        Assertions.assertThat(errorDTO.getCode()).isEqualTo(0);
        Assertions.assertThat(errorDTO.getMessage()).isEqualTo(msg);
        Assertions.assertThat(errorDTO.getStackTrace()).isNotEmpty();
    }
}