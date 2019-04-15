package us.capturecore.core.common.game.data.upgrades;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.capturecore.core.common.player.CCPlayer;

public interface SoloUpgrade {

    static String getName() {
        return "Solo Upgrade";
    }

    static int getCost() {
        return 10;
    }

    static UpgradeCurrenty getCurrency() {
        return UpgradeCurrenty.IRON;
    }

    static Material getIconType() {
        return Material.STONE;
    }

    static void give(Player player) {
        give(new CCPlayer(player));
    }

    static void give(CCPlayer player) {
        if (!player.getInventory().contains(getCurrency().getMaterial(), getCost())) {
            player.sendMessage("&cYou do not have enough " + getCurrency().getName() + " (" + getCost() + ") for this.");
            player.playSound(Sound.NOTE_BASS_DRUM);
            return;
        }

        player.getInventory().addItem(new ItemStack(getIconType()));

        player.sendMessage("&aYou purchased &6" + getName() + "&a!");
        player.playSound(Sound.NOTE_PIANO);
    }

}
