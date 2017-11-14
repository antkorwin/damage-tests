package com.antkorvin.damagetests.utils;

import com.antkorvin.damagetests.utills.sqltracker.AssertSqlCount;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.database.rider.core.DBUnitRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by Korovin Anatolii on 15.11.17.
 *
 * Run all context configuration with database rider rule.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseSystemIT {

    @Autowired
    protected MockMvc mockMvc;

    protected ObjectMapper mapper = new ObjectMapper();

    //region init for DataSet from the rider's database tool
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Rule
    public DBUnitRule dbUnitRule = DBUnitRule.instance(() -> jdbcTemplate.getDataSource().getConnection());
    //endregion

    @Before
    public void setUp() throws Exception {
        AssertSqlCount.reset();
    }

}
