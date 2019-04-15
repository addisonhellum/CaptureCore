package us.capturecore.core.commands.general;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.Main;
import us.capturecore.core.util.text.ChatUtil;

import java.util.Map;

public class LobbyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(ChatUtil.format("&cYou must be a player to do this!"));
            return false;
        }

        Player player = (Player) s;
        player.teleport(Location.deserialize((Map<String, Object>) Main.getInstance().getConfig().get("lobby-location")));

        return false;
    }

}
