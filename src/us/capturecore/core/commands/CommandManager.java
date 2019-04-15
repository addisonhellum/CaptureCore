package us.capturecore.core.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.common.player.CCPlayer;

import java.util.*;

public class CommandManager {

    private String name;
    private Map<String, String> commands = new HashMap<>();

    public CommandManager(String baseCommandName) {
        this.name = baseCommandName;
    }

    public String getName() {
        return name;
    }

    public Map<String, String> getCommandData() {
        return commands;
    }

    public String getDescription(String command) {
        return getCommandData().get(command);
    }

    public boolean isRegistered(String command) {
        return getCommandData().containsKey(command);
    }

    public List<String> getCommands() {
        List<String> cmds = new ArrayList<>();

        for (String cmd : commands.keySet())
            cmds.add(cmd);

        return cmds;
    }

    public List<String> getCommandInterface(int page) {
        List<String> cmdList = new ArrayList<>();

        int pages = getCommands().size() / 10;

        int beginIndex = page * 10;
        int endIndex = beginIndex + 10;

        if (getCommands().size() < endIndex) endIndex = getCommands().size();

        cmdList.add("&6----------------------------------------------------");
        cmdList.add("&a" + getName() + " Commands (Page " + (page + 1) + "/" + (pages + 1) + "):");
        for (String cmd : getCommands().subList(beginIndex, endIndex))
            cmdList.add("&e/" + cmd + " &7- &b" + getDescription(cmd));
        cmdList.add("&6----------------------------------------------------");

        return cmdList;
    }

    public void sendInterface(CommandSender sender) {
        for (String line : getCommandInterface(0))
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
    }

    public void register(String command, String description) {
        commands.put(command, description);
    }

}
