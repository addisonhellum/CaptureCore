package us.capturecore.core.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.util.text.ChatUtil;

public class CrownsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(ChatUtil.format("&cYou're console, you are infinitely rich."));
            return false;
        }

        CCPlayer player = new CCPlayer((Player) s);
        player.sendMessage("[Your Balance] &eYou have &6" + player.getCrowns() + "&lC");
        player.sendMessage("&eYou can earn crowns by playing games or purchase more from our shop @ &bstore.capturecore.us");

        return false;
    }

}
