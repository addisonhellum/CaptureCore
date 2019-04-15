package us.capturecore.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.Main;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.util.text.ChatUtil;

public class SetlobbyCommand implements CommandExecutor {

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

        Main.getInstance().getConfig().set("lobby-location", player.getLocation().serialize());
        Main.getInstance().saveConfig();

        player.sendMessage("[Lobby] &aThe server's lobby location was successfully updated to your current position.");

        for (Player p : Bukkit.getOnlinePlayers())
            if (Main.getRankManager().getRank(p.getUniqueId()).hasAccess(Rank.HELPER))
                p.sendMessage(ChatUtil.format("&d[STAFF] " + player.getFormattedName() + " &bupdated the server lobby location."));

        return false;
    }

}
