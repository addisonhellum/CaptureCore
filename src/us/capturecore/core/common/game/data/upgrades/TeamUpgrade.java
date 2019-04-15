package us.capturecore.core.common.game.data.upgrades;

import org.bukkit.inventory.ItemStack;
import us.capturecore.core.common.game.data.CCTeam;

import java.util.List;

public interface TeamUpgrade {

    String getName();
    int getCost();
    int getLevel();
    ItemStack getIcon();

    List<CCTeam> getUnlockers();
    void unlock(CCTeam team);

}
