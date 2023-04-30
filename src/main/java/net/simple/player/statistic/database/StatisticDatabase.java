package net.simple.player.statistic.database;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class StatisticDatabase {
    private final DataSource ds;
    private final String table;

    public StatisticDatabase(DataSource ds, String table) {
        this.ds = ds;
        this.table = table;
        init();
    }

    private void init() {
        CompletableFuture.runAsync(() -> {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = ds.getConnection();
                stmt = conn.prepareStatement(
                        "CREATE TABLE IF NOT EXISTS `"+table+"` (`player` VARCHAR(48) NOT NULL, `playtime` FLOAT, " +
                                "`online` BOOLEAN, `lastjoin` DATE, PRIMARY KEY (`player`));"
                );
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void createStatistic(UUID uuid, boolean online) {
        CompletableFuture.runAsync(() -> {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = ds.getConnection();
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                stmt = conn.prepareStatement(
                        "INSERT INTO `"+table+"` (`player`, `playtime`, `online`, `lastjoin`) VALUES " +
                                "('"+uuid.toString()+"', '0', '"+(online ? 1 : 0)+"', '"+timeStamp+"');"
                );
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void updateStatistic(UUID uuid, float playtime, boolean online) {
        CompletableFuture.runAsync(() -> {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = ds.getConnection();
                String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                stmt = conn.prepareStatement("UPDATE `"+table+"` SET `playtime` = '"+playtime+"', " +
                        "`online` = '"+(online ? 1 : 0)+"', `lastjoin` = '"+timeStamp+"' WHERE `"+table+"`.`player` = '"+uuid.toString()+"'");
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CompletableFuture<StatisticTable> getStatistic(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = ds.getConnection();
                stmt = conn.prepareStatement(
                        "SELECT * FROM `"+table+"` WHERE `player` = '"+uuid.toString()+"'"
                );
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return new StatisticTable(
                                uuid,
                                rs.getFloat("playtime"),
                                rs.getBoolean("online"),
                                rs.getDate("lastjoin")
                        );
                    }
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CompletableFuture<Boolean> contains(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = ds.getConnection();
                stmt = conn.prepareStatement(
                        "SELECT * FROM `"+table+"` WHERE `player` LIKE '%"+uuid.toString()+"%'"
                );
                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
