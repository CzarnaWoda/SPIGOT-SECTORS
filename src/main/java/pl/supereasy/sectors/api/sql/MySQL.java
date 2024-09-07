package pl.supereasy.sectors.api.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import pl.supereasy.sectors.SectorPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class MySQL {

    private final SectorPlugin plugin;
    private final HikariDataSource dataSource;
    private final Connection connection;

    public MySQL(final SectorPlugin plugin) {
        this.plugin = plugin;
        this.dataSource = new HikariDataSource();
        int poolSize = Runtime.getRuntime().availableProcessors() * 2 + 1; // (core_count * 2) + spindle [pattern from PostgreSQL wiki]
        this.dataSource.setMaximumPoolSize(poolSize);
        this.dataSource.setConnectionTimeout(30000);


        this.dataSource.setJdbcUrl("jdbc:mysql://" + this.plugin.getCurrentSectorConfig().getMysql_server() + "/" + this.plugin.getCurrentSectorConfig().getMysql_database() + "?autoReconnect=true&useUnicode=yes");
        this.dataSource.setUsername(this.plugin.getCurrentSectorConfig().getMysql_user());
        this.dataSource.setPassword(this.plugin.getCurrentSectorConfig().getMysql_password());
        this.dataSource.addDataSourceProperty("cachePrepStmts", true);
        this.dataSource.addDataSourceProperty("prepStmtCacheSize", 250);
        this.dataSource.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        this.dataSource.addDataSourceProperty("useServerPrepStmts", true);

        try {
            connection = dataSource.getConnection();
            this.plugin.getLogger().log(Level.INFO, "Polaczenie z baza danych zostalo nawiazane!");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Polaczenie z baza danych nie zostalo nawiazane!");
        }
    }


    public ResultSet query(String query) throws SQLException {
        return this.connection.prepareStatement(query).executeQuery();
    }

    public int update(String query) throws SQLException {
        return this.connection.prepareStatement(query).executeUpdate();
    }

    public PreparedStatement generateStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }
}
