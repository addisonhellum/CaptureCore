package us.capturecore.core.common.game.data.upgrades;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum UpgradeCurrenty {

    IRON("Iron", ChatColor.WHITE, Material.IRON_INGOT),
    GOLD("Gold", ChatColor.GOLD, Material.GOLD_INGOT),
    DIAMOND("Diamond", ChatColor.AQUA, Material.DIAMOND),
    EMERALD("Emerald", ChatColor.GREEN, Material.EMERALD);

    private String name;
    private ChatColor color;
    private Material material;

    UpgradeCurrenty(String name, ChatColor color, Material material) {
        this.name = name;
        this.color = color;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }

}
