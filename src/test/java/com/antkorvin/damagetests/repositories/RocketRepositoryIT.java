package com.antkorvin.damagetests.repositories;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.utils.BaseDataJpaIT;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Commit;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;


/**
 * Created by Korovin Anatolii on 12.11.17.
 * <p>
 * DAO level's integration tests. Checking custom query and constraints.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketRepositoryIT extends BaseDataJpaIT {

    @Autowired
    private RocketRepository rocketRepository;


    @Ignore("used this only for generation test data set")
    @Test
    @Commit
    @DataSet(cleanBefore = true)
    @ExportDataSet(format = DataSetFormat.JSON,
                   dependentTables = true,
                   outputName = "target/datasets/rockets.json")
    public void initDataSet() {

        Stream.of("Nuclear", "Atomic", "Empty")
              .map(s -> Rocket.builder()
                              .name(s)
                              .launchCode("" + new Random().nextInt(100000))
                              .build())
              .forEach(rocketRepository::save);
    }


    /**
     * Test constraint on the Rocket entity, trying to save same entity twice, expected exception.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void checkUniqueConstraint() {
        // Arrange
        Rocket.RocketBuilder rocketBuilder = Rocket.builder().name("Rocket").launchCode("000");

        // Act
        rocketRepository.save(rocketBuilder.build());
        rocketRepository.save(rocketBuilder.build());
    }


    @Test
    @DataSet(cleanBefore = true, value = "datasets/rockets.json")
    public void testFindRocket() {
        // Arrange
        // Act
        Optional<Rocket> rocket = rocketRepository.findById(UUID.fromString("f26b93f8-a0ab-496c-8b24-3b446cb1c50c"));

        // Asserts
        Assertions.assertThat(rocket.get())
                  .extracting(Rocket::getName, Rocket::getLaunchCode)
                  .contains("Nuclear", "936");
    }

    @Test
    @DataSet(cleanBefore = true, value = "datasets/rockets.json")
    public void testCustomQueryWithLike() {
        // Arrange
        // Act
        List<Rocket> rockets = rocketRepository.findByNameLike("bomb");

        // Asserts
        Assertions.assertThat(rockets)
                  .hasSize(2)
                  .extracting(Rocket::getName)
                  .containsOnly("Atomic bomb", "Empty bomb");
    }
}