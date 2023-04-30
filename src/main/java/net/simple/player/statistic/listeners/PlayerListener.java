package net.simple.player.statistic.listeners;

import net.simple.player.statistic.database.StatisticDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;
import java.util.UUID;

public class PlayerListener implements Listener {
    private final StatisticDatabase database;

    public PlayerListener(StatisticDatabase database) {
        this.database = database;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        database.contains(uuid).thenAccept(contains -> {
            if (contains) {
                float playtime = ((((float) event.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60) / 60);
                database.updateStatistic(uuid, playtime, true);
            } else {
                database.createStatistic(uuid, true);
            }
        });
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        database.contains(uuid).thenAccept(contains -> {
            if (contains) {
                float playtime = ((((float) event.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE) / 20) / 60) / 60);
                database.updateStatistic(uuid, playtime, false);
            } else {
                database.createStatistic(uuid, false);
            }
        });
    }
}
