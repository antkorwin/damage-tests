package com.antkorvin.damagetests.utils;

import com.antkorvin.damagetests.utills.sqltracker.AssertSqlCount;
import com.github.database.rider.core.DBUnitRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Korovin Anatolii on 14.11.17.
 *
 * Abstract Base test for testing work with the database.
 * Use only for testing DAO level.
 * Does not load entire context configuration.
 *
 * Main test cases:
 * - testing db.schem and constraints
 * - testing queries created by the spring-data, specification, querydsl
 * - testing custom query
 * - asserts sql query count
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseDataIT {

    //region init for DataSet from the rider's database tool
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());

    @Before
    public void setUp() throws Exception {
        AssertSqlCount.reset();
    }
    //endregion

}
