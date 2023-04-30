package net.simple.player.statistic.database;

import java.sql.Date;
import java.util.UUID;

public class StatisticTable {
    private final UUID uuid;
    private final float playtime;
    private final boolean online;
    private final Date lastjoin;

    public StatisticTable(UUID uuid, float playtime, boolean online, Date lastjoin) {
        this.uuid = uuid;
        this.playtime = playtime;
        this.online = online;
        this.lastjoin = lastjoin;
    }

    public UUID getUuid() {
        return uuid;
    }

    public float getPlaytime() {
        return playtime;
    }

    public boolean getOnline() {
        return online;
    }

    public Date getLastJoin() {
        return lastjoin;
    }
}
