package us.capturecore.core.commands.general;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.util.menu.GuiMenu;
import us.capturecore.core.util.menu.ItemStackBuilder;
import us.capturecore.core.util.text.ChatUtil;

public class PlayCommand implements CommandExecutor {

    private static GuiMenu menu;

    public static void initializeMenu() {
        menu = new GuiMenu("Play a Game", 3);

        menu.set(1, 1, new ItemStackBuilder(Material.STAINED_GLASS_PANE).withName("&6Select a Server")
                .withLore(new String[] {
                        "Play with friends more easily", "by joining any server,", "regardless of its status.", "",
                        "&8You will join as a spectator", "&8if the game has started.", "", "&eClick to view servers!"
                }).withData(1));
        menu.bindLeft(10, "play servers");

        menu.set(1, 3, new ItemStackBuilder(Material.BANNER).withName("&aSMALL &8(3v3)")
                .withLore(new String[] {
                        "Capture the Flag with fewer", "players and compact maps.", "", "&7Available maps:",
                        "&7• &bExample Map", "", "&eJoin ??? players!"
                }).withData(1));
        menu.bindLeft(12, "play small");

        menu.set(1, 4, new ItemStackBuilder(Material.BANNER).withName("&aSTANDARD &8(4v4)")
                .withLore(new String[] {
                        "Capture the Flag classic,", "with comfortable maps.", "", "&7Available maps:",
                        "&7• &bExample Map", "", "&eJoin ??? players!"
                }).withData(10));
        menu.bindLeft(13, "play standard");

        menu.set(1, 5, new ItemStackBuilder(Material.BANNER).withName("&aLARGE &8(6v6)")
                .withLore(new String[] {
                        "Capture the Flag with many", "players and spacious maps.", "", "&7Available maps:",
                        "&7• &bExample Map", "", "&eJoin ??? players!"
                }).withData(12));
        menu.bindLeft(14, "play large");

        menu.set(1, 7, new ItemStackBuilder(Material.BANNER).withName("&dSPECIAL &8(Event)")
                .withLore(new String[] {
                        "Special, limited-time modes", "for Capture the Flag!", "", "&7Available modes:",
                        "&7• &cNo Special Modes!", "", "&6Check again later!"
                }).withData(13));
        menu.bindLeft(16, "play special");
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(ChatUtil.format("&cYou must be a player to do this!"));
            return false;
        }

        CCPlayer player = new CCPlayer((Player) s);

        if (args.length == 0) {
            player.openMenu(menu);
        }

        return false;
    }

}
