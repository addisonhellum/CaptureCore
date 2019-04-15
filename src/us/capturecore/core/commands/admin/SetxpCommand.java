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

import java.util.UUID;

public class SetxpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (s instanceof Player) {
            CCPlayer player = new CCPlayer((Player) s);
            if (!player.hasAccess(Rank.ADMIN)) {
                player.sendMessage("&cYou are not allowed to do this!");
                return false;
            }
        }

        if (args.length < 2) {
            s.sendMessage(ChatUtil.format("&cUsage: /setxp <player> <(+/-)amount(L)>"));
            return false;

        } else if (args.length == 2) {
            String name = args[0];
            String amount = args[1];

            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (!Main.getCurrencyManager().isInitialized(uuid)) {
                Main.getCurrencyManager().initializeCrowns(uuid);
                s.sendMessage(ChatUtil.format("&7Initializing experience data..."));
            }

            int value;
            boolean level = false;

            if (amount.contains("L") || amount.contains("l")) {
                level = true;
                amount = amount.replace("L", "").replace("l", "");
            }

            try {
                value = Integer.valueOf(amount);
            } catch (Exception e) {
                s.sendMessage(ChatUtil.format("&cExperience amounts are numerical."));
                s.sendMessage(ChatUtil.format("&cUsage: /setxp <player> <(+/-)amount>"));
                return false;
            }

            String sender = "&c[CONSOLE]";
            if (s instanceof Player) sender = new CCPlayer((Player) s).getFormattedName();
            CCPlayer player = new CCPlayer(uuid);

            if (!level) {
                if (amount.startsWith("+")) {
                    player.giveExperience(value);

                    s.sendMessage(ChatUtil.format("&7You gave &b" + value + " XP &7to " + player.getFormattedName()));
                    if (player.isOnline())
                        player.sendMessage(sender + " &eadded &b" + value + " XP &eto your profile.");

                } else if (amount.startsWith("-")) {
                    if (value > player.getCrowns()) Main.getExperienceManager().setExperience(uuid, 0);
                    Main.getExperienceManager().setExperience(uuid, player.getExperience() - value);

                    s.sendMessage(ChatUtil.format("&7You took &b" + value + " XP &7from " + player.getFormattedName()));
                    if (player.isOnline())
                        player.sendMessage(sender + " &etook &b" + value + " XP &efrom your profile.");

                } else {
                    Main.getExperienceManager().setExperience(uuid, value);

                    s.sendMessage(ChatUtil.format("&7You set " + player.getFormattedName() + "&7's experience to &b" + value + " XP&7."));
                    if (player.isOnline())
                        player.sendMessage(sender + " &eset your experience to &b" + value + " XP&e.");
                }
            } else {
                value = value * 5000;

                if (amount.startsWith("+")) {
                    player.giveExperience(value);

                } else if (amount.startsWith("-")) {
                    if (value > player.getCrowns()) Main.getExperienceManager().setExperience(uuid, 0);
                    Main.getExperienceManager().setExperience(uuid, player.getExperience() - value);

                } else {
                    Main.getExperienceManager().setExperience(uuid, value - 5000);
                }

                s.sendMessage(ChatUtil.format("&7" + player.getFormattedName() + " &7is now lvl. " + player.getLevelFormat()));
                if (player.isOnline()) player.sendMessage(sender + " &eset you to lvl. " + player.getLevelFormat());
            }

            if (player.isOnline()) {
                player.spigot().setLevel(player.getLevel());
                player.spigot().setExp((player.getExperience() % 5000) / 5000);
            }
        }

        return false;
    }

}
