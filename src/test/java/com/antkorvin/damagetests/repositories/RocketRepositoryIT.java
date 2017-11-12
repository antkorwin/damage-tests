package com.antkorvin.damagetests.repositories;

import com.antkorvin.damagetests.models.Rocket;
import com.github.database.rider.core.DBUnitRule;
import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.DataSetFormat;
import com.github.database.rider.core.api.exporter.ExportDataSet;
import org.assertj.core.groups.Tuple;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.stream.Stream;


/**
 * Created by Korovin Anatolii on 12.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RocketRepositoryIT {


    //TODO: move in base clase (test inheritance for RepIT)
    //region init for DataSet from the river's database tool
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());
    //endregion

    @Autowired
    private RocketRepository rocketRepository;


    //TODO: ignoring this
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
                              .launchCode("" + s.hashCode())
                              .build())
              .forEach(rocketRepository::save);
    }


    @Test
    public void checkUniqueConstraint() {
        // Arrange
        UUID id = UUID.randomUUID();
        rocketRepository.save(new Rocket(id, "Rembo", "000"));

        // Act
        //TODO: GuardCheck add in dependency!!

        // Asserts

    }
}