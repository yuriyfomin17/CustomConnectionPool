package org.nimofy;

import java.sql.Connection;
import java.sql.SQLException;

public interface CustomDataSource {
    Connection getConnection() throws SQLException;
    void close() throws SQLException;
}
