package us.capturecore.core.common.game.data;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.*;

public class CCTeam {

    private static List<CCTeam> teams = new ArrayList<>();
    public static List<CCTeam> getTeams() { return teams; }

    public static CCTeam getTeam(UUID uuid) {
        for (CCTeam team : getTeams())
            if (team.getMembers().contains(uuid)) return team;

        return null;
    }

    public static CCTeam getTeam(String teamName) {
        for (CCTeam team : getTeams())
            if (team.getName().equalsIgnoreCase(teamName)) return team;

        return null;
    }

    private String name;
    private ChatColor color;

    private Set<UUID> members = new HashSet<>();

    public CCTeam(String name, ChatColor color) {
        this.name = name;
        this.color = color;

        teams.add(this);
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getPrefix() {
        return getColor() + "[" + getName().toUpperCase() + "] " + ChatColor.RESET;
    }

    public Set<UUID> getMembers() {
        return members;
    }

    public Set<Player> getOnlineMembers() {
        Set<Player> online = new HashSet<>();

        for (UUID member : getMembers())
            if (Bukkit.getOfflinePlayer(member).isOnline()) online.add(Bukkit.getPlayer(member));

        return online;
    }

    public void addMember(UUID uuid) {
        if (getTeam(uuid) != null) getTeam(uuid).removeMember(uuid);
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public boolean hasMember(UUID uuid) {
        return getMembers().contains(uuid);
    }

    public boolean isOnline(Player player) {
        return getOnlineMembers().contains(player);
    }

    public int count() {
        return getMembers().size();
    }

}
