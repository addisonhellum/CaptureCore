package us.capturecore.core.common.player.experience;

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

public class ExperienceManager implements Listener {

    private JavaPlugin plugin;

    public ExperienceManager(JavaPlugin plugin) {
        this.plugin = plugin;

        initializeTable();
        plugin.getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public String getName() { return "Experience"; }
    public String getDescription() { return "Handles player experience on the network."; }
    public String getVersion() { return "0.1-SNAPSHOT"; }

    public void initializeTable() {
        if (SQL.tableExists("experience")) return;
        SQL.createTable( "experience",
                SQL.stringsToStringArray( "uuid", "text" ),
                SQL.stringsToStringArray( "xp", "int" ));
    }

    public void initializeXP(UUID uuid) {
        if (isInitialized(uuid)) return;
        SQL.insertInto("experience",
                new String[] {"uuid", "xp"},
                new String[] {uuid.toString(), "0"});
    }

    public boolean isInitialized(UUID uuid) {
        return SQL.recordExists("experience", "uuid = " + SQL.quote(uuid.toString()));
    }

    public int getExperience(UUID uuid) {
        if (!isInitialized(uuid)) {
            initializeXP(uuid);
            return 0;
        }

        String query = "SELECT * FROM experience WHERE uuid = " + SQL.quote(uuid.toString());
        ResultSet rs = SQL.execute(query);

        try {
            while (rs.next()) {
                return rs.getInt("xp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

        return 0;
    }

    public void setExperience(UUID uuid, int amount) {
        if (!isInitialized(uuid)) {
            initializeXP(uuid);
        }

        String query = "UPDATE experience SET xp = " + amount +
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
        initializeXP(player.getUniqueId());
    }

}
