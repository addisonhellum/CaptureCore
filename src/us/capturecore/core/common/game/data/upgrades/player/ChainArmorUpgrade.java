package us.capturecore.core.common.game.data.upgrades.player;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import us.capturecore.core.common.game.data.upgrades.SoloUpgrade;
import us.capturecore.core.common.game.data.upgrades.UpgradeCurrenty;
import us.capturecore.core.common.player.CCPlayer;

public class ChainArmorUpgrade implements SoloUpgrade {

    public static String getName() {
        return "Chainmail Armor";
    }

    public static int getCost() {
        return 40;
    }

    public static UpgradeCurrenty getCurrency() {
        return UpgradeCurrenty.IRON;
    }

    public static Material getIconType() {
        return Material.CHAINMAIL_CHESTPLATE;
    }

    public static void give(CCPlayer player) {
        player.getInventory().setLeggings(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        player.getInventory().setBoots(new ItemStack(Material.CHAINMAIL_BOOTS));
    }

}
