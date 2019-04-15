package us.capturecore.core;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.capturecore.core.commands.admin.*;
import us.capturecore.core.commands.general.*;
import us.capturecore.core.commands.general.ingame.ShopCommand;
import us.capturecore.core.commands.staff.AdventureCommand;
import us.capturecore.core.commands.staff.CreativeCommand;
import us.capturecore.core.commands.staff.SpectatorCommand;
import us.capturecore.core.commands.staff.SurvivalCommand;
import us.capturecore.core.common.GlobalHandler;
import us.capturecore.core.common.game.GameHandler;
import us.capturecore.core.common.lobby.LobbyHandler;
import us.capturecore.core.common.player.currency.CurrencyManager;
import us.capturecore.core.common.player.experience.ExperienceManager;
import us.capturecore.core.common.player.rank.RankManager;
import us.capturecore.core.event.ServerStartEvent;
import us.capturecore.core.event.ServerStopEvent;
import us.capturecore.core.util.NametagUtil;
import us.capturecore.core.util.SQL;
import us.capturecore.core.util.menu.GuiHandler;
import us.capturecore.core.util.text.TabTitleManager;

/** Copyright (C) CaptureCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Written by Addison Hellum <addisonhellum@gmail.com>, June 2018
 */
public class Main extends JavaPlugin {

    private static Main plugin;
    public static Plugin getInstance() { return plugin; }

    private static RankManager rankManager;
    public static RankManager getRankManager() { return rankManager; }

    private static CurrencyManager currencyManager;
    public static CurrencyManager getCurrencyManager() { return currencyManager; }

    private static ExperienceManager experienceManager;
    public static ExperienceManager getExperienceManager() { return experienceManager; }

    @Override
    public void onEnable() {
        plugin = this;

        FileConfiguration config = getConfig();

        config.options().copyDefaults(true);
        saveConfig();

        SQL.setupConnection(
                config.getString("sql-data.hostname"),
                config.getString("sql-data.port"),
                config.getString("sql-data.database"),
                config.getString("sql-data.username"),
                config.getString("sql-data.password")
        );

        getCommand("help").setExecutor(new HelpCommand());
        getCommand("setrank").setExecutor(new SetrankCommand());
        getCommand("adminoverride").setExecutor(new AoCommand());
        getCommand("survival").setExecutor(new SurvivalCommand());
        getCommand("creative").setExecutor(new CreativeCommand());
        getCommand("adventure").setExecutor(new AdventureCommand());
        getCommand("spectator").setExecutor(new SpectatorCommand());
        getCommand("crowns").setExecutor(new CrownsCommand());
        getCommand("discord").setExecutor(new DiscordCommand());
        getCommand("setlobby").setExecutor(new SetlobbyCommand());
        getCommand("lobby").setExecutor(new LobbyCommand());
        getCommand("chestgiant").setExecutor(new ChestgiantCommand());
        getCommand("setcoins").setExecutor(new SetcoinsCommand());
        getCommand("setxp").setExecutor(new SetxpCommand());
        getCommand("play").setExecutor(new PlayCommand());
        getCommand("partydata").setExecutor(new PartydataCommand());
        getCommand("shop").setExecutor(new ShopCommand());

        getServer().getPluginManager().registerEvents(new GuiHandler(), this);
        getServer().getPluginManager().registerEvents(new TabTitleManager(), this);
        getServer().getPluginManager().registerEvents(new GlobalHandler(), this);

        if (getServerType().equalsIgnoreCase("lobby"))
            getServer().getPluginManager().registerEvents(new LobbyHandler(), this);

        else if (getServerType().equalsIgnoreCase("game"))
            getServer().getPluginManager().registerEvents(new GameHandler(), this);

        NametagUtil.initializeRanks();
        AoCommand.initialize();
        PlayCommand.initializeMenu();

        ServerStartEvent startEvent = new ServerStartEvent(this);
        getServer().getPluginManager().callEvent(startEvent);

        rankManager = new RankManager(this);
        currencyManager = new CurrencyManager(this);
        experienceManager = new ExperienceManager(this);
    }

    @Override
    public void onDisable() {
        SQL.close();

        ServerStopEvent stopEvent = new ServerStopEvent(this);
        getServer().getPluginManager().callEvent(stopEvent);
    }

    public static String getServerType() {
        return Main.getInstance().getConfig().getString("server-type");
    }

}
