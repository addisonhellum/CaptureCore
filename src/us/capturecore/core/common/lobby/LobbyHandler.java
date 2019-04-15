package us.capturecore.core.common.lobby;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.SkullMeta;
import us.capturecore.core.Main;
import us.capturecore.core.commands.admin.AoCommand;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.util.menu.ItemStackBuilder;
import us.capturecore.core.util.text.ChatUtil;

import java.util.Arrays;

public class LobbyHandler implements Listener {

    private Location lobbyLocation;

    public Location getLobbyLocation() {
        if (lobbyLocation == null) {
            ConfigurationSection loc = Main.getInstance().getConfig().getConfigurationSection("lobby-location");
            lobbyLocation = new Location(Bukkit.getWorld(loc.getString("world")),
                    loc.getDouble("x"), loc.getDouble("y"), loc.getDouble("z"),
                    (float) loc.getDouble("yaw"), (float) loc.getDouble("pitch"));
        }

        return lobbyLocation;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CCPlayer player = new CCPlayer(event.getPlayer());

        event.setJoinMessage(null);
        if (player.hasAccess(Rank.PREMIUM)) {
            event.setJoinMessage(player.getRank().getPrefix() + player.getName() + ChatColor.YELLOW + " joined the lobby!");
            player.spigot().setAllowFlight(true);
            player.sendMessage(" ");
        }

        player.spigot().setFoodLevel(20);
        player.spigot().setHealth(20);

        player.spigot().setLevel(player.getLevel());
        player.spigot().setExp((player.getExperience() % 5000) / 5000);

        player.teleport(getLobbyLocation());

        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setItem(1, new ItemStackBuilder(Material.NETHER_STAR).withName("&bPlay a Game &7(Right Click)")
                .withLore("Click to open game selection menu.").build());
        inv.setItem(7, new ItemStackBuilder(Material.INK_SACK).withName("&fPlayers: &aVISIBLE &7(Click to Toggle)")
                .withLore("Click to toggle player visibility to &cHIDDEN&7.").withData(10));

        ItemStack profile = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) profile.getItemMeta();
        meta.setOwner(player.getName());
        meta.setDisplayName(ChatUtil.format("&aMy Profile Menu &7(Right Click)"));
        meta.setLore(Arrays.asList(new String[] {ChatUtil.format("&7Click to open your profile menu.")}));
        profile.setItemMeta(meta);

        inv.setItem(8, profile);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action a = event.getAction();
        ItemStack icon = event.getItem();

        if (icon == null) return;
        if (icon.getItemMeta().getDisplayName() == null) return;
        if (icon.getType().equals(Material.INK_SACK) && icon.getItemMeta().getDisplayName().contains("VISIBLE")) {
            player.getInventory().setItem(7, new ItemStackBuilder(Material.INK_SACK)
                    .withName("&fPlayers: &cHIDDEN &7(Click to Toggle)")
                    .withLore("Click to toggle player visibility to &aVISIBLE&7.").withData(8));

            for (Player p : Bukkit.getOnlinePlayers()) {
                CCPlayer ccp = new CCPlayer(p);

                if (!ccp.hasAccess(Rank.TWITCH)) player.hidePlayer(p);
            }
        }
        if (icon.getType().equals(Material.INK_SACK) && icon.getItemMeta().getDisplayName().contains("HIDDEN")) {
            player.getInventory().setItem(7, new ItemStackBuilder(Material.INK_SACK)
                    .withName("&fPlayers: &aVISIBLE &7(Click to Toggle)")
                    .withLore("Click to toggle player visibility to &cHIDDEN&7.").withData(10));

            for (Player p : Bukkit.getOnlinePlayers())
                player.showPlayer(p);
        }
        if (icon.getType().equals(Material.NETHER_STAR) && icon.getItemMeta().getDisplayName().contains("Play a Game")) {
            player.performCommand("play");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (AoCommand.hasOverride(player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (AoCommand.hasOverride(player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (AoCommand.hasOverride(player)) return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        event.setFoodLevel(20);
        event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (AoCommand.hasOverride(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (AoCommand.hasOverride(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPainting(PaintingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getRemover();
        if (!AoCommand.hasOverride(player)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDamager(EntityDamageByEntityEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onTest(PlayerToggleSneakEvent event) {
        CCPlayer player = new CCPlayer(event.getPlayer());
        if (!event.isSneaking()) return;


    }

}
