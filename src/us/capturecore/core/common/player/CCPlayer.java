package us.capturecore.core.common.player;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import us.capturecore.core.Main;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.util.NametagUtil;
import us.capturecore.core.util.menu.GuiMenu;
import us.capturecore.core.util.text.ActionBar;
import us.capturecore.core.util.text.ChatUtil;

import java.util.UUID;

/** Copyright (C) CaptureCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Written by Addison Hellum <addisonhellum@gmail.com>, June 2018
 */
public class CCPlayer {

    private UUID uuid;
    private Player player;
    private Rank rank;

    public CCPlayer(UUID uuid) {
        this.uuid = uuid;
        this.player = Bukkit.getPlayer(uuid);
        this.rank = Main.getRankManager().getRank(uuid);
    }

    public CCPlayer(Player player) {
        this.uuid = player.getUniqueId();
        this.player = player;
        this.rank = Main.getRankManager().getRank(uuid);
    }

    public UUID getUniqueId() { return uuid; }

    public boolean isOnline() {
        return Bukkit.getOfflinePlayer(getUniqueId()).isOnline();
    }

    public boolean hasAccess(Rank rank) {
        return getRank().hasAccess(rank) || spigot().isOp();
    }

    public String getName() {
        return Bukkit.getOfflinePlayer(getUniqueId()).getName();
    }

    public String getFormattedName() {
        return getRank().getPrefix() + getName();
    }

    public Rank getRank() {
        return rank;
    }

    public PlayerInventory getInventory() { return spigot().getInventory(); }

    public Location getLocation() { return spigot().getLocation(); }

    public int getCrowns() { return Main.getCurrencyManager().getCrowns(getUniqueId()); }

    public int getLevel() { return Math.floorDiv(getExperience(), 5000) + 1; }

    public int getExperience() {
        return Main.getExperienceManager().getExperience(getUniqueId());
    }

    public void giveCoins(int amount) {
        Main.getCurrencyManager().setCrowns(getUniqueId(), getCrowns() + amount);
    }

    public void giveExperience(int amount) {
        Main.getExperienceManager().setExperience(getUniqueId(), getExperience() + amount);
    }

    public void openMenu(GuiMenu menu) {
        menu.display(spigot());
    }

    public String getLevelFormat() {
        int level = getLevel();

        ChatColor color = ChatColor.GRAY;
        if (level >= 100) color = ChatColor.WHITE;
        if (level >= 200) color = ChatColor.GOLD;
        if (level >= 300) color = ChatColor.AQUA;
        if (level >= 400) color = ChatColor.DARK_RED;

        return ChatUtil.format(color + "[" + level + "✫]");
    }

    public Player spigot() {
        return player;
    }

    public void teleport(Location location) {
        spigot().teleport(location);
    }

    public void sendMessage(String message) {
        if (!isOnline()) return;
        spigot().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void playSound(Sound sound) {
        spigot().playSound(getLocation(), sound, 1F, 1F);
    }

    public void sendActionBar(String message) {
        ActionBar.sendActionBarMessage(spigot(), ChatColor.translateAlternateColorCodes('&', message));
    }

    public void setRank(Rank rank) {
        Main.getRankManager().setRank(getUniqueId(), rank);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (isOnline()) NametagUtil.setNametag(spigot(), rank);
            }
        }.runTaskLater(Main.getInstance(), 20);
    }

    public void updateCaches() {
        this.rank = Main.getRankManager().getRank(getUniqueId());
        if (!isOnline()) this.player = null;
    }

}
