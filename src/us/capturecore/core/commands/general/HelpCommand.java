package us.capturecore.core.commands.general;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import us.capturecore.core.util.text.ChatUtil;

public class HelpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        s.sendMessage(" ");
        s.sendMessage(ChatUtil.format("[Help] &aNeed help? &eRead this > &bwww.capturecore.us/support"));

        return false;
    }

}
