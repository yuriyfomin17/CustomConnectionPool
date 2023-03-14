package org.nimofy;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class Util {

    public static DataSource initializeDataSource(){
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setURL("jdbc:postgresql://localhost:5432/postgres");
        return dataSource;
    }

    public static CustomDataSource initializeCustomDataSource(){
        return new PolledDataSource("jdbc:postgresql://localhost:5432/postgres", "nimofy1797", "", 10);
    }
}
