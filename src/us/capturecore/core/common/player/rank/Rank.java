package us.capturecore.core.common.player.rank;

import org.bukkit.ChatColor;
import org.bukkit.Color;

/** Copyright (C) CaptureCore, Inc - All Rights Reserved
 *  Unauthorized copying of this file, via any medium is strictly prohibited
 *  Written by Addison Hellum <addisonhellum@gmail.com>, June 2018
 */
public enum Rank {

    DEFAULT(0, "Player", "", ChatColor.WHITE, ChatColor.GRAY, Color.GRAY),
    PREMIUM(1, "Premium", "Premium", ChatColor.AQUA, ChatColor.WHITE, Color.AQUA),
    ELITE(2, "Elite", "Elite", ChatColor.GOLD, ChatColor.WHITE, Color.ORANGE),
    TWITCH(3, "Twitch", "Twitch", ChatColor.DARK_PURPLE, ChatColor.WHITE, Color.PURPLE),
    YOUTUBE(4, "YouTube", "YT", ChatColor.RED, ChatColor.WHITE, Color.RED),
    HELPER(5, "Helper", "Helper", ChatColor.BLUE, ChatColor.WHITE, Color.BLUE),
    MOD(6, "Moderator", "Mod", ChatColor.DARK_GREEN, ChatColor.WHITE, Color.GREEN),
    ADMIN(7, "Administrator", "Admin", ChatColor.RED, ChatColor.WHITE, Color.RED),
    OWNER(8, "Owner", "Owner", ChatColor.DARK_RED, ChatColor.WHITE, Color.RED);

    public int id;
    public String formal;
    public String abbrev;
    public ChatColor rColor;
    public ChatColor tColor;
    public Color c;

    Rank(int id, String formalName, String abbreviated, ChatColor rankColor, ChatColor textColor, Color color) {
        this.id = id;
        formal = formalName;
        abbrev = abbreviated;
        rColor = rankColor;
        tColor = textColor;
        c = color;
    }

    public int getId() { return id; }

    public String getFormalName() {
        return formal;
    }

    public String getAbbreviatedName() {
        return abbrev;
    }

    public ChatColor getRankColor() {
        return rColor;
    }

    public ChatColor getTextColor() {
        return tColor;
    }

    public Color getColor() {
        return c;
    }

    public String getPrefix() {
        if (getAbbreviatedName() == "") return ChatColor.GRAY + "";
        return getRankColor() + "[" + getAbbreviatedName() + "] ";
    }

    public String formatMessage(String username, String message) {
        if (getAbbreviatedName() == "") return ChatColor.WHITE + username + getTextColor() + ": " + message;
        return getPrefix() + username + getTextColor() + ": " + message;
    }

    public boolean hasAccess(Rank rank) {
        return getId() >= rank.getId();
    }

    public static Rank fromString(String name) {
        for (Rank rank : Rank.values()) {
            if (name.equalsIgnoreCase(rank.getFormalName())) return rank;
            if (name.equalsIgnoreCase(rank.getAbbreviatedName())) return rank;
        }

        if (Rank.valueOf(name) != null) return Rank.valueOf(name);
        return Rank.DEFAULT;
    }

    public static boolean isValid(String name) {
        for (Rank rank : Rank.values()) {
            if (name.equalsIgnoreCase(rank.getFormalName())) return true;
            if (name.equalsIgnoreCase(rank.getAbbreviatedName())) return true;
        }

        if (Rank.valueOf(name) != null) return true;
        return false;
    }

}
