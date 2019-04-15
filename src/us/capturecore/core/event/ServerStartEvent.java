package us.capturecore.core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/** Copyright (C) CaptureCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Written by Addison Hellum <addisonhellum@gmail.com>, June 2018
 */
public class ServerStartEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private JavaPlugin plugin;

    /**
     * Called within the onEnable method.
     * @param plugin The plugin that was enabled.
     */
    public ServerStartEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}