package us.capturecore.core.commands.general.ingame;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.Main;
import us.capturecore.core.common.game.gameutil.ShopMenu;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.util.text.ChatUtil;

public class ShopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (s instanceof Player) {
            CCPlayer player = new CCPlayer((Player) s);

            if (!player.hasAccess(Rank.ADMIN)) {
                player.sendMessage("&cYou are not allowed to do this!");
                return false;
            }
        }

        if (!Main.getServerType().equalsIgnoreCase("game")) {
            s.sendMessage(ChatUtil.format("&cYou can only perform this command on a game server."));
        }

        if (args.length == 0) {
            s.sendMessage(ChatUtil.format("&cUsage: /shop <Player> <Solo/Team>"));
            return false;

        } else if (args.length == 1) {
            s.sendMessage(ChatUtil.format("&cUsage: /shop <Player> <Solo/Team>"));
            return false;

        } else if (args.length == 2) {
            String target = args[0];
            String type = args[1];

            if (type.equalsIgnoreCase("solo") || type.equalsIgnoreCase("team")) {
                if (!Bukkit.getOfflinePlayer(target).isOnline()) {
                    s.sendMessage(ChatUtil.format("&cThat player is not online!"));
                    return false;
                }

                Player player = Bukkit.getPlayer(target);
                if (type.equalsIgnoreCase("solo")) {
                    ShopMenu.getArcheryTab().display(player);

                } else if (type.equalsIgnoreCase("team")) {
                    s.sendMessage(ChatUtil.format("&cWork in progress."));
                }

            } else {
                s.sendMessage(ChatUtil.format("&cInvalid shop type. Expected [Solo/Team]."));
            }

        } else {
            s.sendMessage(ChatUtil.format("&cUsage: /shop <Player> <Solo/Team>"));
            return false;
        }

        return false;
    }

}
