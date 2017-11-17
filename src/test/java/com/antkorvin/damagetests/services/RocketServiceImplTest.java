package com.antkorvin.damagetests.services;

import com.antkorvin.damagetests.errorinfos.RocketServiceErrorInfo;
import com.antkorvin.damagetests.exceptions.ConditionValidationException;
import com.antkorvin.damagetests.exceptions.NotFoundException;
import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.models.RocketStatus;
import com.antkorvin.damagetests.repositories.RocketRepository;
import com.antkorvin.damagetests.utils.GuardCheck;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import junitparams.naming.TestCaseName;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Korovin Anatolii on 11.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RunWith(JUnitParamsRunner.class)
public class RocketServiceImplTest {

    @InjectMocks
    private RocketServiceImpl rocketService;

    @Mock
    private RocketRepository rocketRepository;

    private ArgumentCaptor<Rocket> rocketCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        rocketCaptor = ArgumentCaptor.forClass(Rocket.class);
    }


    @Test
    public void create() {
        // Arrange
        Rocket rocket = mock(Rocket.class);
        String boom = "boom";
        when(rocketRepository.save(any(Rocket.class))).thenReturn(rocket);

        // Act
        Rocket result = rocketService.create(boom);

        // Asserts
        Assertions.assertThat(result)
                  .isNotNull()
                  .isEqualTo(rocket);

        ArgumentCaptor<Rocket> captor = ArgumentCaptor.forClass(Rocket.class);
        verify(rocketRepository).save(captor.capture());

        Assertions.assertThat(captor.getValue())
                  .extracting(Rocket::getName)
                  .contains(boom);
    }

    @Test
    public void checkLaunchCodeUniqueAfterCreate() {

        // Act
        rocketService.create("Spring");
        rocketService.create("Java");

        // Asserts
        verify(rocketRepository, times(2)).save(rocketCaptor.capture());

        Stream<String> codes = rocketCaptor.getAllValues()
                                           .stream()
                                           .map(Rocket::getLaunchCode)
                                           .peek(System.out::println)
                                           .distinct();

        Assertions.assertThat(codes)
                  .hasSize(2);
    }


    @Test
    public void checkCodeFormat() {

        // Arrange
        int rocketCounter = 100;

        // Act
        IntStream.range(0, rocketCounter)
                 .mapToObj(String::valueOf)
                 .parallel()
                 .forEach(rocketService::create);

        // Asserts
        verify(rocketRepository, times(rocketCounter)).save(rocketCaptor.capture());

        Assertions.assertThat(rocketCaptor.getAllValues())
                  .extracting(Rocket::getLaunchCode)
                  .allMatch(s -> {
                      System.out.println(s);
                      return true;
                  })
                  .allMatch(s -> Pattern.matches("^[a-f0-9]{5}$", s));
    }


    @Test
    public void testGetRocket() {
        // Arrange
        UUID id = UUID.randomUUID();
        Rocket rocket = mock(Rocket.class);
        when(rocketRepository.findById(id)).thenReturn(Optional.of(rocket));

        // Act
        Rocket result = rocketService.get(id);

        // Asserts
        Assertions.assertThat(result)
                  .isNotNull()
                  .isEqualTo(rocket);

        verify(rocketRepository).findById(eq(id));
    }

    /**
     * Test get request for the not existed entity, expected exception
     */
    @Test
    public void testGetNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(rocketRepository.findById(id)).thenReturn(Optional.empty());

        // Act & assert
        GuardCheck.check(() -> rocketService.get(id),
                         NotFoundException.class,
                         RocketServiceErrorInfo.ROCKET_NOT_FOUND);
    }


    @Test
    @Parameters({"REACTIVE", "ULTRASOUND"})
    @TestCaseName("[{index}] {method}: Rocket.status={params}")
    public void fireTest(RocketStatus status) {
        // Arrange
        UUID id = UUID.randomUUID();
        Rocket rocket = Rocket.builder()
                              .status(status)
                              .build();

        when(rocketRepository.findById(eq(id))).thenReturn(Optional.of(rocket));

        ArgumentCaptor<Rocket> captor = ArgumentCaptor.forClass(Rocket.class);

        // Act
        rocketService.fire(id);

        // Asserts
        verify(rocketRepository).save(captor.capture());
        Assertions.assertThat(captor.getValue())
                  .extracting(Rocket::getStatus)
                  .contains(RocketStatus.USED);
    }


    @Test
    public void testFireOnAlreadyUsedRocket() {
        UUID id = UUID.randomUUID();
        Rocket rocket = Rocket.builder()
                              .status(RocketStatus.USED)
                              .build();

        when(rocketRepository.findById(eq(id))).thenReturn(Optional.of(rocket));

        // Act
        GuardCheck.check(() -> rocketService.fire(id),
                         ConditionValidationException.class,
                         RocketServiceErrorInfo.IMPOSSIBLE_FIRE_ROCKET_ALREADY_USED);
    }


    @Test
    public void testGetAllByStatus() {
        // Arrange
        Rocket rocket1 = mock(Rocket.class);
        Rocket rocket2 = mock(Rocket.class);
        when(rocketRepository.findAllByStatus(RocketStatus.USED))
                .thenReturn(Arrays.asList(rocket1, rocket2));

        // Act
        List<Rocket> rockets = rocketService.getAllByStatus(RocketStatus.USED);

        // Asserts
        verify(rocketRepository).findAllByStatus(RocketStatus.USED);

        Assertions.assertThat(rockets)
                  .isNotNull()
                  .hasSize(2)
                  .contains(rocket1, rocket2);
    }


    @Test
    public void testCharge() {
        // Arrange
        UUID id = UUID.randomUUID();

        Rocket rocket = Rocket.builder()
                              .status(RocketStatus.USED)
                              .name("emptiness")
                              .launchCode("1234")
                              .id(id)
                              .build();

        when(rocketRepository.findById(id)).thenReturn(Optional.of(rocket));
        ArgumentCaptor<Rocket> captor = ArgumentCaptor.forClass(Rocket.class);

        when(rocketRepository.save(any(Rocket.class))).thenReturn(rocket);

        // Act
        Rocket result = rocketService.charge(id);

        // Asserts
        Assertions.assertThat(result).isEqualTo(rocket);

        verify(rocketRepository).save(captor.capture());
        Assertions.assertThat(captor.getValue())
                  .isEqualTo(rocket)
                  .extracting(Rocket::getStatus)
                  .doesNotContain(RocketStatus.USED);
    }
}