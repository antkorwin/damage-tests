package com.antkorvin.damagetests.repositories;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.utills.sqltracker.AssertSqlCount;
import com.antkorvin.damagetests.utils.BaseDataIT;
import com.antkorvin.damagetests.utils.BaseSystemIT;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;


/**
 * Created by Korovin Anatolii on 12.11.17.
 * <p>
 * DAO level's integration tests. Checking custom query, constraints and sql count.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class RocketRepositoryIT extends BaseDataIT {

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
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Test(expected = DataIntegrityViolationException.class)
    @DataSet(cleanBefore = true)
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

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DataSet(cleanBefore = true)
    public void checkSqlQueryCountWhileCreateNewInstance() {
        // Arrange
        Rocket rocket1 = Rocket.builder().name("Rocket_1").launchCode("000").build();
        Rocket rocket2 = Rocket.builder().name("Rocket_2").launchCode("000").build();

        // Act
        rocketRepository.save(rocket1);
        rocketRepository.save(rocket2);

        // Asserts
        AssertSqlCount.assertSelectCount(0);
        AssertSqlCount.assertInsertCount(2);
    }


    @Test
    @DataSet(cleanBefore = true)
    public void checkSqlQueryCountWhileReadRocket() {
        // Arrange
        // Act
        List<Rocket> rockets = rocketRepository.findByNameLike("bomb");

        // Asserts
        AssertSqlCount.assertSelectCount(1);
    }
}