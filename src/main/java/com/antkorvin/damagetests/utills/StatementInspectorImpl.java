package com.antkorvin.damagetests.utills;

import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * Created by Korovin Anatolii on 14.11.17.
 *
 * @author Korovin Anatolii
 * @version 1.0
 */
public class StatementInspectorImpl implements StatementInspector {

    @Override
    public String inspect(String sql) {
        // TODO: counting different sql query here
        return sql;
    }
}
