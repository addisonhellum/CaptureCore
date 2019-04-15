package us.capturecore.core.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.capturecore.core.util.text.ChatUtil;

public class DiscordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        s.sendMessage(ChatUtil.format("[Discord] &3Join the community @ &bdiscord.capturecore.us"));
        s.sendMessage(ChatUtil.format(" > &6Earn free Crowns by linking your discord now!"));

        return false;
    }

}
