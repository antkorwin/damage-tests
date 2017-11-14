package com.antkorvin.damagetests.utills.sqltracker;

import org.hibernate.resource.jdbc.spi.StatementInspector;

/**
 * Created by Korovin Anatolii on 14.11.17.
 *
 * We using statement inspector for handling and counting sql query
 *
 * @author Igor Dmitriev / Mikalai Alimenkou
 * @author Korovin Anatolii
 * @version 1.0
 */
public class StatementInspectorImpl implements StatementInspector {

    private static final QueryHandler QUERY_HANDLER = new QueryCountInfoHandler();

    @Override
    public String inspect(String sql) {
        QUERY_HANDLER.handleSql(sql);
        return sql;
    }
}
