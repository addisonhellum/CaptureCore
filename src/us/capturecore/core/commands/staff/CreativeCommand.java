package us.capturecore.core.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.Main;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.util.text.ChatUtil;

public class CreativeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("creative")) {
            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cYou must be a player to do this!"));
                return false;
            }

            CCPlayer player = new CCPlayer((Player) s);

            if (!player.getRank().hasAccess(Rank.MOD)) {
                player.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
                return false;
            }

            if (args.length == 0) {
                player.spigot().setGameMode(GameMode.CREATIVE);
                player.sendMessage("&7Your gamemode has been updated to &b[Creative]");
            } else {
                String name = args[0];
                OfflinePlayer op = Bukkit.getOfflinePlayer(name);

                if (!Main.getRankManager().isInitialized(op.getUniqueId())) {
                    player.sendMessage("&cThat player was not found in our database.");
                    return false;
                }

                if (!op.isOnline()) {
                    player.sendMessage("&cThat player is not currently online.");
                    return false;
                }

                CCPlayer p = new CCPlayer(Bukkit.getPlayer(name));
                p.spigot().setGameMode(GameMode.CREATIVE);

                p.sendMessage(player.getFormattedName() + " &7updated your gamemode to &b[Creative]");
                player.sendMessage("&7You updated " + p.getFormattedName() + "&7's gamemode to &b[Creative]");
            }
        }

        return false;
    }

}
