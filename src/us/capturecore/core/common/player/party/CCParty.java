package us.capturecore.core.common.player.party;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import us.capturecore.core.common.player.CCPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CCParty {

    private static List<CCParty> parties = new ArrayList<>();
    public static List<CCParty> getParties() { return parties; }

    public static CCParty getByLeader(UUID leader) {
        for (CCParty party : getParties())
            if (party.getLeader().equals(leader)) return party;

        return null;
    }

    public static CCParty getParty(UUID member) {
        for (CCParty party : getParties())
            if (party.hasMember(member)) return party;

        return null;
    }

    private UUID leader;

    private List<UUID> members = new ArrayList<>();

    public CCParty(UUID leader) {
        this.leader = leader;
        parties.add(this);
    }

    public UUID getLeader() {
        return leader;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public List<Player> getOnlineMembers() {
        List<Player> online = new ArrayList<>();

        for (UUID uuid : getMembers())
            if (Bukkit.getOfflinePlayer(uuid).isOnline())
                online.add(Bukkit.getPlayer(uuid));

        return online;
    }

    public boolean hasMember(UUID uuid) {
        return members.contains(uuid);
    }

    public void addMember(UUID uuid) {
        if (!hasMember(uuid))
            members.add(uuid);
    }

    public void kickMember(UUID uuid) {
        if (hasMember(uuid))
            members.remove(uuid);
    }

    public void disband() {
        parties.remove(this);
    }

}
