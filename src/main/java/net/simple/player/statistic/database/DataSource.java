package net.simple.player.statistic.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {
    private final HikariConfig config = new HikariConfig();
    private HikariDataSource ds;

    public DataSource(String ip, String port, String name, String user, String password) {
        init(ip, port, name, user, password);
    }

    private void init(String ip, String port, String name, String user, String password) {
        if (ds != null) return;
        config.setJdbcUrl("jdbc:mysql://"+ip+":"+port+"/"+name);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.addDataSourceProperty("minimumIdle", "5");
        config.addDataSourceProperty("idleTimeout", "600000");
        config.addDataSourceProperty("poolName", "HikariCorePool");
        config.addDataSourceProperty("connectionTestQuery", "select * from information_schema.tables limit 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        ds = new HikariDataSource( config );
    }

    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    public void close() {
        if (!ds.isClosed()) ds.close();
    }
}
