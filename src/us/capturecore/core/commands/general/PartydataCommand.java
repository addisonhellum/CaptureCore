package us.capturecore.core.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.common.player.party.CCParty;
import us.capturecore.core.util.text.ChatUtil;

import java.util.UUID;

public class PartydataCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (args.length == 2) {
            String operation = args[0];
            String uuidString = args[1];

            if (operation.equalsIgnoreCase("leader")) {
                new CCParty(UUID.fromString(uuidString));

            } else if (operation.equalsIgnoreCase("join")) {
                if (!(s instanceof Player)) {
                    s.sendMessage(ChatUtil.format("&cYou must be a player to do this!"));
                    return false;
                }

                Player player = (Player) s;
                CCParty.getByLeader(UUID.fromString(uuidString)).addMember(player.getUniqueId());
            }
        }

        return false;
    }

}
