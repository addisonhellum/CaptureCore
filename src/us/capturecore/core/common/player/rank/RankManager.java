package us.capturecore.core.common.player.rank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import us.capturecore.core.Main;
import us.capturecore.core.common.Common;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.event.ServerStartEvent;
import us.capturecore.core.util.NametagUtil;
import us.capturecore.core.util.SQL;

import java.sql.ResultSet;
import java.util.UUID;

/** Copyright (C) CaptureCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Written by Addison Hellum <addisonhellum@gmail.com>, June 2018
 */
public class RankManager implements Common, Listener {

    private JavaPlugin plugin;

    public RankManager(JavaPlugin plugin) {
        this.plugin = plugin;

        initializeTable();
        plugin.getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public String getName() { return "Ranks"; }
    public String getDescription() { return "Handles player ranks on the network."; }
    public String getVersion() { return "0.1-SNAPSHOT"; }

    public void initializeTable() {
        if (SQL.tableExists("ranks")) return;
        SQL.createTable( "ranks",
                SQL.stringsToStringArray( "uuid", "text" ),
                SQL.stringsToStringArray( "rank", "text" ));
    }

    public void initializeRank(UUID uuid) {
        if (isInitialized(uuid)) return;
        SQL.insertInto("ranks",
                new String[] {"uuid", "rank"},
                new String[] {uuid.toString(), Rank.DEFAULT.getFormalName()});

        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Player player = Bukkit.getPlayer(uuid);
            NametagUtil.setNametag(player, Rank.DEFAULT);
        }
    }

    public boolean isInitialized(UUID uuid) {
        return SQL.recordExists("ranks", "uuid = " + SQL.quote(uuid.toString()));
    }

    public Rank getRank(UUID uuid) {
        if (!isInitialized(uuid)) {
            initializeRank(uuid);
            return Rank.DEFAULT;
        }

        String query = "SELECT * FROM ranks WHERE uuid = " + SQL.quote(uuid.toString());
        ResultSet rs = SQL.execute(query);

        try {
            while (rs.next()) {
                return Rank.fromString(rs.getString("rank"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Rank.DEFAULT;
        }

        return Rank.DEFAULT;
    }

    public void setRank(UUID uuid, Rank rank) {
        if (!isInitialized(uuid)) {
            initializeRank(uuid);
        }

        String query = "UPDATE ranks SET rank = " + SQL.quote(rank.getFormalName()) +
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
        initializeRank(player.getUniqueId());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        CCPlayer player = new CCPlayer(event.getPlayer());
        String message = event.getMessage();

        // TODO: Cache ranks.
        Rank rank = getRank(player.getUniqueId());

        message = message.replace("%", "%%");
        if (rank.hasAccess(Rank.ADMIN)) message = ChatColor.translateAlternateColorCodes('&', message);

        event.setFormat(player.getLevelFormat() + " " + rank.formatMessage(player.getName(), message));
    }

}
