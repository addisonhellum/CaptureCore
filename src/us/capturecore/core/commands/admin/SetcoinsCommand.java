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

public class SetcoinsCommand implements CommandExecutor {

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
            s.sendMessage(ChatUtil.format("&cUsage: /setcoins <player> <(+/-)amount>"));
            return false;

        } else if (args.length == 2) {
            String name = args[0];
            String amount = args[1];

            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();
            if (!Main.getCurrencyManager().isInitialized(uuid)) {
                Main.getCurrencyManager().initializeCrowns(uuid);
                s.sendMessage(ChatUtil.format("&7Initializing currency data..."));
            }

            int value;
            try {
                value = Integer.valueOf(amount);
            } catch (Exception e) {
                s.sendMessage(ChatUtil.format("&cCurrency amounts are numerical."));
                s.sendMessage(ChatUtil.format("&cUsage: /setcoins <player> <(+/-)amount>"));
                return false;
            }

            String sender = "&c[CONSOLE]";
            if (s instanceof Player) sender = new CCPlayer((Player) s).getFormattedName();
            CCPlayer player = new CCPlayer(uuid);

            if (amount.startsWith("+")) {
                player.giveCoins(value);

                s.sendMessage(ChatUtil.format("&7You gave &6" + value + "&lC &7to " + player.getFormattedName()));
                if (player.isOnline()) player.sendMessage(sender + " &eadded &6" + value + "&lC &eto your balance.");

            } else if (amount.startsWith("-")) {
                if (value > player.getCrowns()) Main.getCurrencyManager().setCrowns(uuid, 0);
                Main.getCurrencyManager().setCrowns(uuid, player.getCrowns() - value);

                s.sendMessage(ChatUtil.format("&7You took &6" + value + "&lC &7from " + player.getFormattedName()));
                if (player.isOnline()) player.sendMessage(sender + " &etook &6" + value + "&lC &efrom your balance.");

            } else {
                Main.getCurrencyManager().setCrowns(uuid, value);

                s.sendMessage(ChatUtil.format("&7You set " + player.getFormattedName() + "&7's crowns to &6" + value + "&lC"));
                if (player.isOnline()) player.sendMessage(sender + " &eset your crowns to &6" + value + "&lC");
            }
        }

        return false;
    }

}
