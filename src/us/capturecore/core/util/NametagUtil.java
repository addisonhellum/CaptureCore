package us.capturecore.core.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.capturecore.core.common.player.rank.Rank;

public class NametagUtil {

    private static boolean ranksInit = false;
    private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public static void initializeRanks() {
        for (Rank rank : Rank.values()) {
            if (scoreboard.getTeam(rank.getFormalName()) == null) {
                Team team = scoreboard.registerNewTeam(rank.getFormalName());
                team.setPrefix(rank.getPrefix());
            }
        }

        ranksInit = true;
    }

    public static void setNametag(Player player, Rank rank) {
        if (!ranksInit) return;

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join " +
                rank.getFormalName() + " " + player.getName());
    }

    public static void setNametag(Player player, String teamName, String prefix, String suffix) {
        if (scoreboard.getTeam(teamName) == null) {
            Team team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(prefix);
            team.setSuffix(suffix);
        }

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "scoreboard teams join " +
                teamName + " " + player.getName());
    }

}
