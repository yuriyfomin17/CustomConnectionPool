package org.nimofy;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PolledDataSource implements CustomDataSource {
    private static final int CONNECTION_LIMIT = 151;
    private final String url;
    private final String username;
    private final String password;
    private final int queueLimit;
    private final Queue<ConnectionProxy> connectionPool = new ConcurrentLinkedDeque<>();

    public PolledDataSource(String url, String username, String password, int queueLimit) {

        if (queueLimit > CONNECTION_LIMIT) {
            throw new RuntimeException("You have over reached maximum queue limit of 151");
        }
        this.url = url;
        this.username = username;
        this.password = password;
        this.queueLimit = queueLimit;
        initializeConnectionQueue();
    }

    private void initializeConnectionQueue() {
        for (int i = 0; i < this.queueLimit; i++) {
            try {
                var physicalConnection = this.getPhysicalConnection();
                var logicalConnection = new ConnectionProxy(physicalConnection, connectionPool);
                connectionPool.add(logicalConnection);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public Connection getConnection() {
        return connectionPool.poll();
    }

    @Override
    public void close() throws SQLException {
        while (this.connectionPool.size() != 0){
            ConnectionProxy connection = this.connectionPool.poll();
            connection.terminateConnection();
        }
    }

    private Connection getPhysicalConnection() throws SQLException {
        return DriverManager.getConnection(this.url, username, password);
    }
}
