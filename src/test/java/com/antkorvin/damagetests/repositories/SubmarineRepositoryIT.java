package com.antkorvin.damagetests.repositories;

import com.antkorvin.damagetests.models.Rocket;
import com.antkorvin.damagetests.models.Submarine;
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
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Korovin Anatolii on 14.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@Transactional
public class SubmarineRepositoryIT extends BaseDataIT {

    @Autowired
    private SubmarineRepository submarineRepository;

    @Ignore
    @Test
    @Commit
    @DataSet(cleanBefore = true)
    @ExportDataSet(format = DataSetFormat.JSON, dependentTables = true, outputName = "target/datasets/submarine.json")
    public void dataSetGenerate() {
        // Arranger
        Rocket rocket1 = Rocket.builder().name("Rocket_1").launchCode("000").build();
        Rocket rocket2 = Rocket.builder().name("Rocket_2").launchCode("000").build();
        Rocket rocket3 = Rocket.builder().name("Rocket_3").launchCode("000").build();
        Submarine ussr = Submarine.builder().name("USSR").rocketList(Arrays.asList(rocket1, rocket2, rocket3)).build();

        Rocket rocket4 = Rocket.builder().name("Rocket_4").launchCode("000").build();
        Rocket rocket5 = Rocket.builder().name("Rocket_5").launchCode("000").build();
        Submarine usa = Submarine.builder().name("USA").rocketList(Arrays.asList(rocket4, rocket5)).build();

        // Act
        submarineRepository.save(ussr);
        submarineRepository.save(usa);
    }

    @Test
    @Commit
    @DataSet(cleanBefore = true, value = "datasets/submarine.json")
    public void testNPlusOneCase() {
        // Arrange
        // Act
        List<Submarine> submarines = submarineRepository.findByPowerGreaterThan(9);

        submarines.forEach(submarine -> submarine.getRocketList().size());

        // Asserts
        Assertions.assertThat(submarines)
                  .hasSize(2)
                  .extracting(Submarine::getName)
                  .containsOnly("UA", "USSR");

        AssertSqlCount.assertSelectCount(3);
    }


    @Test
    @Commit
    @DataSet(cleanBefore = true, value = "datasets/submarine.json")
    public void testNPlusOneFix() {
        // Arrange
        // Act
        List<Submarine> submarines = submarineRepository.findByPowerGreaterThanFix(9);

        submarines.forEach(submarine -> submarine.getRocketList().size());

        // Asserts
        Assertions.assertThat(submarines)
                  .hasSize(2)
                  .extracting(Submarine::getName)
                  .containsOnly("UA", "USSR");

        AssertSqlCount.assertSelectCount(1);
    }
}