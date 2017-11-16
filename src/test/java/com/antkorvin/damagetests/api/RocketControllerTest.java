package com.antkorvin.damagetests.api;

import com.antkorvin.damagetests.api.dto.RocketDTO;
import com.antkorvin.damagetests.mappers.RocketMapperImpl;
import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.services.RocketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Korovin Anatolii on 11.11.17.
 * <p>
 * Unit-test RocketController
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketControllerTest {

    @Mock
    private RocketService rocketService;

    private MockMvc mockMvc;
    private ObjectMapper mapper = new ObjectMapper();

    private UUID id = UUID.randomUUID();
    private String name = "jelly-belly";
    private String code = "q1w2e3";
    private Rocket rocket = Rocket.builder()
                                  .id(id)
                                  .name(name)
                                  .launchCode(code)
                                  .build();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RocketController rocketController = new RocketController(rocketService, new RocketMapperImpl());
        mockMvc = MockMvcBuilders.standaloneSetup(rocketController)
                                 .alwaysDo(MockMvcResultHandlers.print())
                                 .build();
    }


    @Test
    public void create() throws Exception {
        // Arrange
        when(rocketService.create(eq(name))).thenReturn(rocket);

        // Act
        String resultContent = mockMvc.perform(post("/{url}/create", RocketController.URL)
                                                       .param("name", name))
                                      .andExpect(status().isCreated())
                                      .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                                      .andReturn().getResponse().getContentAsString();

        // Asserts
        RocketDTO result = mapper.readValue(resultContent, RocketDTO.class);
        assertRocketDTO(result, rocket);

        verify(rocketService).create(name);
    }


    @Ignore("пример того что нельзя протестировать в юнит тесте не поднимая контекст")
    @Test
    public void testCreateWithThrow() throws Exception {
        // Arrange
        when(rocketService.create(any(String.class)))
                .thenThrow(IndexOutOfBoundsException.class);

        // Act & Assert
        mockMvc.perform(post("/{url}/create", RocketController.URL)
                                .param("name", "123"))
               .andExpect(status().isInternalServerError());
    }


    @Test
    public void testGetRocket() throws Exception {
        // Arrange
        when(rocketService.get(eq(id))).thenReturn(rocket);

        // Act
        String content = mockMvc.perform(get("/{url}/{id}", RocketController.URL, id))
                                .andExpect(status().isOk())
                                .andReturn().getResponse().getContentAsString();

        // Asserts
        RocketDTO result = mapper.readValue(content, RocketDTO.class);
        assertRocketDTO(result, rocket);

        verify(rocketService).get(eq(id));
    }


    private void assertRocketDTO(RocketDTO actual, Rocket expected) {
        Assertions.assertThat(actual)
                  .extracting(RocketDTO::getId,
                              RocketDTO::getName,
                              RocketDTO::getLaunchCode)
                  .contains(expected.getId(),
                            expected.getName(),
                            expected.getLaunchCode());
    }
}