package net.simple.player.statistic.config;

import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;

@ConfigName("config.yml")
public interface MainConfig extends Config {
    @Comment("Включить плагин")
    default boolean getEnablePlugin() {
        return false;
    }

    @Comment("IP адресс от MySql")
    default String getMysqlIp() {
        return "127.0.0.1";
    }
    @Comment("Порт от MySql")
    default String getMysqlPort() {
        return "3306";
    }
    @Comment("Название Базы Данных")
    default String getDbName() {
        return "name";
    }
    @Comment("Пользователь Базы Данных")
    default String getDbUser() {
        return "user";
    }
    @Comment("Пароль пользователя Базы Данных")
    default String getDbPassword() {
        return "password";
    }
    @Comment("Название таблицы в Базе Данных")
    default String getDbTable() {
        return "table";
    }
}
