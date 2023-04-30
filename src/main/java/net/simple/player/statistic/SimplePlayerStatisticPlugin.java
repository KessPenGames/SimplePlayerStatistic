package net.simple.player.statistic;

import net.simple.player.statistic.config.MainConfig;
import net.simple.player.statistic.database.DataSource;
import net.simple.player.statistic.database.StatisticDatabase;
import net.simple.player.statistic.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.plugin.java.JavaPlugin;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

import java.util.UUID;

public final class SimplePlayerStatisticPlugin extends JavaPlugin {
    private MainConfig config;
    private StatisticDatabase database;

    @Override
    public void onEnable() {
        config = ConfigAPI.init(MainConfig.class, NameStyle.UNDERSCORE, CommentStyle.INLINE, true, this);
        if (!config.getEnablePlugin()) {
            getLogger().info("Plugin not enabled in config, please turn plugin.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        registerDatabase();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(database), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            UUID uuid = player.getUniqueId();
            database.contains(uuid).thenAccept(contains -> {
                if (contains) {
                    float playtime = ((((float) player.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60) / 60);
                    database.updateStatistic(uuid, playtime, false);
                } else {
                    database.createStatistic(uuid, false);
                }
            });
        });
    }

    private void registerDatabase() {
        DataSource ds = new DataSource(
                config.getMysqlIp(), config.getMysqlPort(), config.getDbName(), config.getDbUser(), config.getDbPassword()
        );
        database = new StatisticDatabase(ds, config.getDbTable());
        getLogger().info("Connected to MySql!");
    }
}
