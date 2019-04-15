package us.capturecore.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import us.capturecore.core.Main;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.event.ServerStartEvent;
import us.capturecore.core.util.SQL;
import us.capturecore.core.util.text.ActionBar;
import us.capturecore.core.util.text.ChatUtil;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AoCommand implements CommandExecutor, Listener {

    public static void initialize() {
        Main.getInstance().getServer().getPluginManager().registerEvents(new AoCommand(), Main.getInstance());
        startHUDs();

        if (SQL.tableExists("ao_toggle")) return;
        SQL.createTable( "ao_toggle",
                SQL.stringsToStringArray( "uuid", "text" ),
                SQL.stringsToStringArray( "status", "boolean" ));
    }

    private static void setupPlayer(UUID uuid) {
        if (isSetup(uuid)) return;
        SQL.insertInto("ao_toggle",
                new String[] {"uuid", "status"},
                new String[] {uuid.toString(), "0"});
    }

    private static boolean isSetup(UUID uuid) {
        return SQL.recordExists("ao_toggle", "uuid = " + SQL.quote(uuid.toString()));
    }

    public static boolean hasOverride(Player player) {
        if (!isSetup(player.getUniqueId())) {
            setupPlayer(player.getUniqueId());
            return false;
        }

        String query = "SELECT * FROM ao_toggle WHERE uuid = " + SQL.quote(player.getUniqueId().toString());
        ResultSet rs = SQL.execute(query);

        try {
            while (rs.next()) {
                return rs.getBoolean("status");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public static void setStatus(Player player, boolean status) {
        if (!isSetup(player.getUniqueId())) {
            setupPlayer(player.getUniqueId());
        }

        int statusIndex = 0;
        if (status) {
            statusIndex = 1;
            overrides.add(player);
        } else {
            overrides.remove(player);
        }

        String query = "UPDATE ao_toggle SET status = " + statusIndex +
                " WHERE uuid = " + SQL.quote(player.getUniqueId().toString());
        SQL.execute(query);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(ChatUtil.format("&cYou must be a player to do this!"));
            return false;
        }

        CCPlayer player = new CCPlayer((Player) s);

        if (!player.hasAccess(Rank.ADMIN)) {
            player.sendMessage("You are not allowed to do this!");
            return false;
        }

        if (args.length == 0) {
            boolean status = !hasOverride(player.spigot());
            setStatus(player.spigot(), status);

            if (status) {
                player.sendMessage("&eYou have enabled &c[Admin Override]&e. You will now bypass server restrictions.");
            } else {
                player.sendMessage("&eYou have disabled &c[Admin Override]&e. You will no longer bypass server restrictions.");
            }
        }

        return false;
    }

    private static List<Player> overrides = new ArrayList<>();

    private static void startHUDs() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : overrides)
                    ActionBar.sendActionBarMessage(player, "&eYou're in &c[Admin Override] &emode.");
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (hasOverride(player) && !overrides.contains(player)) overrides.add(player);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (overrides.contains(player)) overrides.remove(player);
    }

    @EventHandler
    public void onStart(ServerStartEvent event) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (hasOverride(p)) overrides.add(p);
        }
    }

}
