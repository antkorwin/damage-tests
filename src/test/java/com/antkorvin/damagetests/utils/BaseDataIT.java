package com.antkorvin.damagetests.utils;

import com.antkorvin.damagetests.utills.sqltracker.AssertSqlCount;
import com.github.database.rider.core.DBUnitRule;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;

/**
 * Created by Korovin Anatolii on 14.11.17.
 * <p>
 * Abstract Base test for testing work with the database. Use only for testing DAO level. Does not load entire context
 * configuration.
 * <p>
 * Main test cases: - testing db.schem and constraints - testing queries created by the spring-data, specification,
 * querydsl - testing custom query - asserts sql query count
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class BaseDataIT {

    //region init for DataSet from the rider's database tool
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    private Connection connection;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> {
        connection = jdbcTemplate.getDataSource().getConnection();
        return connection;
    });

    @After
    public void tearDown() throws Exception {
        connection.close();
    }

    @Before
    public void setUp() throws Exception {
        AssertSqlCount.reset();
    }
    //endregion

}
