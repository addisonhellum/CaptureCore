package us.capturecore.core.common.player.currency;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import us.capturecore.core.Main;
import us.capturecore.core.event.ServerStartEvent;
import us.capturecore.core.util.SQL;

import java.sql.ResultSet;
import java.util.UUID;

public class CurrencyManager implements Listener {

    private JavaPlugin plugin;

    public CurrencyManager(JavaPlugin plugin) {
        this.plugin = plugin;

        initializeTable();
        plugin.getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public String getName() { return "Currency"; }
    public String getDescription() { return "Handles player currency on the network."; }
    public String getVersion() { return "0.1-SNAPSHOT"; }

    public void initializeTable() {
        if (SQL.tableExists("currency")) return;
        SQL.createTable( "currency",
                SQL.stringsToStringArray( "uuid", "text" ),
                SQL.stringsToStringArray( "crowns", "int" ));
    }

    public void initializeCrowns(UUID uuid) {
        if (isInitialized(uuid)) return;
        SQL.insertInto("currency",
                new String[] {"uuid", "crowns"},
                new String[] {uuid.toString(), "0"});
    }

    public boolean isInitialized(UUID uuid) {
        return SQL.recordExists("currency", "uuid = " + SQL.quote(uuid.toString()));
    }

    public int getCrowns(UUID uuid) {
        if (!isInitialized(uuid)) {
            initializeCrowns(uuid);
            return 0;
        }

        String query = "SELECT * FROM currency WHERE uuid = " + SQL.quote(uuid.toString());
        ResultSet rs = SQL.execute(query);

        try {
            while (rs.next()) {
                return rs.getInt("crowns");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return 0;
    }

    public void setCrowns(UUID uuid, int amount) {
        if (!isInitialized(uuid)) {
            initializeCrowns(uuid);
        }

        String query = "UPDATE currency SET crowns = " + amount +
                " WHERE uuid = " + SQL.quote(uuid.toString());
        SQL.execute(query);
    }

    @EventHandler
    public void onServerStart(ServerStartEvent event) {
        initializeTable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        initializeCrowns(player.getUniqueId());
    }

}
