package us.capturecore.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import org.bukkit.inventory.ItemStack;
import us.capturecore.core.Main;
import us.capturecore.core.commands.CommandManager;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.util.menu.GuiMenu;
import us.capturecore.core.util.menu.ItemStackBuilder;
import us.capturecore.core.util.text.ChatUtil;

import java.util.UUID;

public class SetrankCommand implements CommandExecutor {

    private CommandManager manager = new CommandManager("Setrank");

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (s instanceof Player) {
            CCPlayer ccp = new CCPlayer((Player) s);
            if (!ccp.hasAccess(Rank.ADMIN)) {
                s.sendMessage(ChatUtil.format("&cYou are not allowed to do this!"));
                return false;
            }
        }

        if (args.length == 0) {
            manager.register("setrank <player>", "Opens the rank-setting GUI.");
            manager.register("setrank <player> [rank]", "Set a player's rank manually.");
            manager.sendInterface(s);

        } else if (args.length == 1) {
            String name = args[0];
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

            if (!Main.getRankManager().isInitialized(uuid)) {
                s.sendMessage(ChatUtil.format("&cThat player could not be found in our database."));
                return false;
            }

            if (!(s instanceof Player)) {
                s.sendMessage(ChatUtil.format("&cOnly players can do this!"));
                return false;
            }

            Player player = (Player) s;
            CCPlayer ccp = new CCPlayer(uuid);

            GuiMenu menu = new GuiMenu("Setting a Rank", 4);
            menu.set(4, new ItemStackBuilder(Material.SKULL_ITEM)
                    .withName(ccp.getRank().getPrefix() + ccp.getName())
                    .withLore(new String[] {
                            "Setting a rank for " + name + ".", "", "&eClick any of the ranks below", "&eto update player data."
                    }).withOwner(name).withData(3));

            Rank[] rankOrder = new Rank[] {Rank.DEFAULT, Rank.PREMIUM, Rank.TWITCH, Rank.HELPER, Rank.ADMIN,
                                           Rank.ELITE, Rank.YOUTUBE, Rank.MOD};

            menu.lockColumn(2).lockColumn(4).lockColumn(6).lockColumn(8).lockRow(1).lockRow(4).lockSlot(18).lockSlot(26);

            for (Rank rank : rankOrder) {
                ItemStack icon = new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName(rank.getPrefix() + name)
                        .withLore(new String[] {
                                "Click to update rank for " + rank.getRankColor() + rank.getFormalName() + "&7."
                        }).enchantIf(Enchantment.ARROW_INFINITE, ccp.getRank().equals(rank)).withData(0);

                menu.add(icon);
                menu.bindLeft(icon, "setrank " + name + " " + rank.getFormalName());
            }

            menu.freezeItems(true);
            menu.display(player);

        } else if (args.length == 2) {
            String name = args[0];
            String toRank = args[1];
            UUID uuid = Bukkit.getOfflinePlayer(name).getUniqueId();

            if (!Main.getRankManager().isInitialized(uuid)) {
                s.sendMessage(ChatUtil.format("&cThat player could not be found in our database."));
                return false;
            }

            CCPlayer ccp = new CCPlayer(uuid);

            if (!Rank.isValid(toRank)) {
                s.sendMessage(ChatUtil.format("&cThat rank does not exist. Try using the GUI!"));
                return false;
            }

            Rank rank = Rank.fromString(toRank);
            ccp.setRank(rank);
            ccp.updateCaches();

            s.sendMessage(ChatUtil.format("&aSuccessfully updated rank to " + ccp.getFormattedName() + "&a!"));
        }

        return false;
    }

}
