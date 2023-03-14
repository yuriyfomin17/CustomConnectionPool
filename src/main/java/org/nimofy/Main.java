package org.nimofy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        CustomDataSource dataSource = Util.initializeCustomDataSource();
        long starTime = System.currentTimeMillis();
        for (int i = 0; i < 10_000; i++) {
            try (Connection connection = dataSource.getConnection()) {

                PreparedStatement preparedStatement = connection.prepareStatement("select * from products");
                preparedStatement.executeQuery();
                ResultSet resultSet = preparedStatement.getResultSet();
                while (resultSet.next()){}


            }
        }
        long timeElapsed = (System.currentTimeMillis() - starTime);
        System.out.println("TIME_ELAPSED:" + timeElapsed + " ms");

        dataSource.close();
    }
}