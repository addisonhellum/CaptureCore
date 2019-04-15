package us.capturecore.core.common.game;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.capturecore.core.Main;
import us.capturecore.core.commands.admin.AoCommand;
import us.capturecore.core.common.game.data.CCMap;
import us.capturecore.core.common.game.data.CCTeam;
import us.capturecore.core.common.game.gameutil.ShopMenu;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.party.CCParty;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.event.ServerStartEvent;
import us.capturecore.core.event.ServerStopEvent;
import us.capturecore.core.util.LocSerialization;
import us.capturecore.core.util.NametagUtil;
import us.capturecore.core.util.menu.ItemStackBuilder;
import us.capturecore.core.util.text.ChatUtil;
import us.capturecore.core.util.text.Title;

import java.io.File;
import java.util.*;

public class GameHandler implements Listener {

    public enum GameState {
        PREGAME, COUNTDOWN, INGAME, ENDED;
    }

    private static GameState gameState = GameState.PREGAME;
    public static GameState getGameState() { return gameState; }

    private static List<Player> players = new ArrayList<>();
    public static List<Player> getPlayers() { return players; }

    private static CCMap map;
    public static CCMap getMap() { return map; }

    private List<Block> placedByPlayer = new ArrayList<>();

    public static void initializeMapData() {
        File mapdir = new File(Main.getInstance().getDataFolder() + "/mapdata");
        if (!mapdir.exists()) mapdir.mkdir();

        //map = CCMap.randomMap();
        map = CCMap.randomMap();
    }

    public static void healPlayer(Player player) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }

    private static CCTeam redTeam = new CCTeam("Red", ChatColor.RED);
    private static CCTeam blueTeam = new CCTeam("Blue", ChatColor.BLUE);
    private static CCTeam spectator = new CCTeam("Spectator", ChatColor.GRAY);

    public static CCTeam getRedTeam() { return redTeam; }
    public static CCTeam getBlueTeam() { return blueTeam; }
    public static CCTeam getSpectator() { return spectator; }

    public Location getSpawn(Player player) {
        CCTeam team = CCTeam.getTeam(player.getUniqueId());

        if (team.getName().equalsIgnoreCase("red")) return getMap().getRedSpawn();
        else if (team.getName().equalsIgnoreCase("blue")) return getMap().getBlueSpawn();
        else return getMap().getLobbyLocation();
    }

    public void joinTeam(Player player, String teamName) {
        UUID uuid = player.getUniqueId();
        CCTeam team = CCTeam.getTeam(teamName);

        team.addMember(uuid);
        NametagUtil.setNametag(player, teamName, team.getColor() + "" + ChatColor.BOLD +
                teamName.toUpperCase().substring(0, 1) + " " + team.getColor(), "");

        if (teamName.equalsIgnoreCase("red")) player.teleport(getMap().getRedSpawn());
        if (teamName.equalsIgnoreCase("blue")) player.teleport(getMap().getBlueSpawn());
    }

    public void assignTeams() {
        int teamSize = getPlayers().size() / 2;

        for (CCParty party : CCParty.getParties()) {
            for (Player player : party.getOnlineMembers()) {
                if (redTeam.count() + party.getOnlineMembers().size() <= teamSize) joinTeam(player, "red");
                else if (blueTeam.count() + party.getOnlineMembers().size() <= teamSize) joinTeam(player, "blue");
            }
        }

        int index = 1;
        for (Player player : getPlayers()) {
            if (CCTeam.getTeam(player.getUniqueId()) == null) {
                if (index % 2 == 0) joinTeam(player, "red");
                else if (index % 2 == 1) joinTeam(player, "blue");
                index++;
            }
        }
    }

    private String div = "&a&l▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬";

    public void broadcastStartMessage() {
        for (Player player : getPlayers()) {
            player.sendMessage(ChatUtil.format(div));
            ChatUtil.sendCenteredMessage(player, "&f&lCapture the Flag");
            player.sendMessage(" ");
            ChatUtil.sendCenteredMessage(player, "&e&lDefend your flag and capture the enemy's flag.");
            ChatUtil.sendCenteredMessage(player, "&e&lUpgrade yourself and your team by collecting");
            ChatUtil.sendCenteredMessage(player, "&e&lIron, Gold, Emerald, and Diamond from mining");
            ChatUtil.sendCenteredMessage(player, "&e&lresources around the map.");
            player.sendMessage(" ");
            player.sendMessage(ChatUtil.format(div));
        }
    }

    public void giveStartingGear(Player player) {
        String team = CCTeam.getTeam(player.getUniqueId()).getName();

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        Color color = Color.GRAY;
        if (team.equalsIgnoreCase("blue")) color = Color.fromRGB(51, 76, 178);
        if (team.equalsIgnoreCase("red")) color = Color.fromRGB(153, 51, 51);

        PlayerInventory inv = player.getInventory();

        inv.setChestplate(new ItemStackBuilder(Material.LEATHER_CHESTPLATE).withColor(color).build());
        inv.setLeggings(new ItemStackBuilder(Material.LEATHER_LEGGINGS).withColor(color).build());
        inv.setBoots(new ItemStackBuilder(Material.LEATHER_BOOTS).withColor(color).build());

        inv.setItem(0, new ItemStackBuilder(Material.WOOD_SWORD).build());
        inv.setItem(1, new ItemStackBuilder(Material.GOLD_PICKAXE).withEnchantment(Enchantment.DIG_SPEED, 3).build());
        inv.setItem(8, new ItemStackBuilder(Material.BLAZE_POWDER).withName("&bAbilities").build());

        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 10000000, 0));

        if (team.equalsIgnoreCase("red")) inv.setItem(2, new ItemStack(Material.WOOL, 64, (byte) 14));
        if (team.equalsIgnoreCase("blue")) inv.setItem(2, new ItemStack(Material.WOOL, 64, (byte) 11));
    }

    @EventHandler
    public void onStart(ServerStartEvent event) {
        initializeMapData();
        ShopMenu.initialize();

        Main.getInstance().getServer().getPluginManager().registerEvents(new ShopMenu(), Main.getInstance());

        for (Player p : Bukkit.getOnlinePlayers()) players.add(p);
    }

    @EventHandler
    public void onStop(ServerStopEvent event) {
        for (Block b : placedByPlayer)
            b.setType(Material.AIR);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CCPlayer player = new CCPlayer(event.getPlayer());

        players.add(player.spigot());
        event.setJoinMessage(ChatUtil.format(player.getFormattedName() + " &ehas joined (&b" + players.size() + "&e/"
                + "&b" + getMap().getMaximumPlayers() + "&e)!"));

        healPlayer(player.spigot());

        if (getPlayers().size() >= getMap().getMinimumPlayers() && getGameState().equals(GameState.PREGAME)) {
            gameState = GameState.COUNTDOWN;

            new BukkitRunnable() {
                int index = 10;

                @Override
                public void run() {
                    if (!getGameState().equals(GameState.COUNTDOWN)) {
                        cancel();
                        return;
                    }

                    if (index == 10) Bukkit.broadcastMessage(ChatUtil.format("&eThe game starts in &a10 &eseconds!"));

                    if (index == 0) {
                        gameState = GameState.INGAME;

                        assignTeams();
                        broadcastStartMessage();

                        for (Player player : getPlayers())
                            giveStartingGear(player);

                        return;
                    }

                    if (index <= 5 && index > 1) {
                        Bukkit.broadcastMessage(ChatUtil.format("&eThe game starts in &b" + index + " &eseconds!"));
                    } else if (index == 1) Bukkit.broadcastMessage(ChatUtil.format("&eThe game starts in &b1 &esecond!"));

                    index--;
                }
            }.runTaskTimer(Main.getInstance(), 20, 20);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CCPlayer player = new CCPlayer(event.getPlayer());

        players.remove(player.spigot());
        event.setQuitMessage(ChatUtil.format(player.getFormattedName() + " &ehas quit!"));

        if (getPlayers().size() < getMap().getMinimumPlayers() && getGameState().equals(GameState.COUNTDOWN)) {
            gameState = GameState.PREGAME;
            Bukkit.broadcastMessage(ChatUtil.format("&cStart cancelled. Waiting for more players."));
        }
    }

    @EventHandler
    public void onFoodLevel(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!getGameState().equals(GameState.INGAME)) event.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (AoCommand.hasOverride(player)) return;

        if (!getGameState().equals(GameState.INGAME)) {
            event.setCancelled(true);
            return;
        }

        int cooldown;

        Material mat = block.getType();
        if (mat.equals(Material.IRON_BLOCK)) {
            player.getInventory().addItem(new ItemStack(Material.IRON_INGOT));
            cooldown = 8;

        } else if (mat.equals(Material.GOLD_BLOCK)) {
            player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT));
            cooldown = 16;

        } else if (mat.equals(Material.DIAMOND_BLOCK)) {
            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
            cooldown = 30;

        } else if (mat.equals(Material.EMERALD_BLOCK)) {
            player.getInventory().addItem(new ItemStack(Material.EMERALD));
            cooldown = 60;
        } else {
            if (placedByPlayer.contains(block)) {
                placedByPlayer.remove(block);
                return;
            }
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);
        block.setType(Material.BEDROCK);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(mat);
            }
        }.runTaskLater(Main.getInstance(), cooldown * 20);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if (!AoCommand.hasOverride(player)) {
            if (!getGameState().equals(GameState.INGAME)) {
                event.setCancelled(true);
                return;
            }
        }

        if (block.getType().equals(Material.TNT)) {
            block.getLocation().getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT);
            event.setCancelled(true);
        }

        placedByPlayer.add(block);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        event.setCancelled(true);

        if (!getGameState().equals(GameState.INGAME)) return;

        for (Block block : event.blockList())
            if (placedByPlayer.contains(block)) {
                block.setType(Material.AIR);
                placedByPlayer.remove(block);
            }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!getGameState().equals(GameState.INGAME)) return;

        Player player = event.getPlayer();
        CCPlayer ccp = new CCPlayer(player);

        String message = event.getMessage();
        if (ccp.hasAccess(Rank.ADMIN)) message = ChatColor.translateAlternateColorCodes('&', message);

        event.setFormat(ccp.getLevelFormat() + " " + CCTeam.getTeam(ccp.getUniqueId()).getPrefix() +
                ccp.getFormattedName() + ChatColor.RESET + ": " + message.replace("%", "%%"));
    }

    public void playRespawn(Player player) {
        PlayerInventory inv = player.getInventory();
        inv.clear();
        inv.setArmorContents(null);

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20);
        player.setAllowFlight(true);
        player.setFlying(true);
        player.teleport(player.getLocation().clone().add(0, 5, 0));
        Bukkit.getOnlinePlayers().forEach(p -> p.hidePlayer(player));

        new BukkitRunnable() {
            int index = 5;

            @Override
            public void run() {
                if (index == 0) {
                    player.sendMessage(ChatUtil.format("&eYou have respawned."));

                    Title title = new Title("", "&eYou have respawned.");
                    title.send(player);

                    player.setGameMode(GameMode.SURVIVAL);
                    giveStartingGear(player);
                    Bukkit.getOnlinePlayers().forEach(p -> p.showPlayer(player));
                    player.teleport(getSpawn(player));
                    player.setAllowFlight(false);
                    player.setFlying(false);

                    cancel();

                } else if (index > 1) {
                    Title title = new Title("&c&lYOU DIED!", "&eYou will respawn in &c" + index + " &eseconds!");
                    title.send(player);

                    player.sendMessage(ChatUtil.format("&eYou will respawn in &c" + index + " &eseconds!"));
                } else {
                    Title title = new Title("&c&lYOU DIED!", "&eYou will respawn in &c1 &esecond!");
                    title.send(player);

                    player.sendMessage(ChatUtil.format("&eYou will respawn in &c1 &esecond!"));
                }

                index--;
            }
        }.runTaskTimer(Main.getInstance(), 5, 20);
    }

    @EventHandler
    public void onFallDamage(EntityDamageEvent event) {
        if (event.getCause().equals(EntityDamageEvent.DamageCause.FALL))
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerKill(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if (entity instanceof Player && damager instanceof Player) {
            Player player = (Player) entity;
            CCTeam team = CCTeam.getTeam(player.getUniqueId());

            if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                event.setCancelled(true);
                return;
            }

            if (event.getFinalDamage() >= player.getHealth()) {
                Player killer = (Player) damager;

                event.setCancelled(true);

                playRespawn(player);

                if (team.getName().equalsIgnoreCase("red"))
                    Bukkit.broadcastMessage(ChatUtil.format("&c" + player.getName() + " &7was killed by &9" + killer.getName()));
                if (team.getName().equalsIgnoreCase("blue"))
                    Bukkit.broadcastMessage(ChatUtil.format("&9" + player.getName() + " &7was killed by &c" + killer.getName()));
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDamageEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;
            CCTeam team = CCTeam.getTeam(player.getUniqueId());

            if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                event.setCancelled(true);
                return;
            }

            if (event.getFinalDamage() >= player.getHealth()) {
                event.setCancelled(true);

                playRespawn(player);

                if (team.getName().equalsIgnoreCase("red"))
                    Bukkit.broadcastMessage(ChatUtil.format("&c" + player.getName() + " &7died!"));
                if (team.getName().equalsIgnoreCase("blue"))
                    Bukkit.broadcastMessage(ChatUtil.format("&9" + player.getName() + " &7died!"));
            }
        }
    }

    @EventHandler
    public void onVoid(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getBlockY() <= 10)
            playRespawn(event.getPlayer());
    }

    private boolean redStolen = false;
    private boolean blueStolen = false;

    @EventHandler
    public void onStealFlag(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();

        if (!getGameState().equals(GameState.INGAME)) return;
        CCTeam team = CCTeam.getTeam(player.getUniqueId());

        if (team.getName().equalsIgnoreCase("red")) {
            if (blueStolen) return;

            blueStolen = true;
            if (loc.distanceSquared(getMap().getBlueFlag()) < 2.5) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatUtil.format("Flag Stolen > &9Blue Flag &7was stolen by &c" + player.getName()));
                Bukkit.broadcastMessage("");
            }

        } else if (team.getName().equalsIgnoreCase("blue")) {
            if (redStolen) return;

            redStolen = true;
            if (loc.distanceSquared(getMap().getRedFlag()) < 2.5) {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(ChatUtil.format("Flag Stolen > &cRed Flag &7was stolen by &9" + player.getName()));
                Bukkit.broadcastMessage("");
            }

        }
    }

}
