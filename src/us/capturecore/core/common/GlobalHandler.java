package us.capturecore.core.common;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import us.capturecore.core.Main;
import us.capturecore.core.util.SQL;

public class GlobalHandler implements Listener {

    @EventHandler
    public void onRenewSQL(PlayerJoinEvent event) {
        FileConfiguration config = Main.getInstance().getConfig();

        if (!SQL.hasOpenConnection()) {
            SQL.setupConnection(
                    config.getString("sql-data.hostname"),
                    config.getString("sql-data.port"),
                    config.getString("sql-data.database"),
                    config.getString("sql-data.username"),
                    config.getString("sql-data.password")
            );
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

}
