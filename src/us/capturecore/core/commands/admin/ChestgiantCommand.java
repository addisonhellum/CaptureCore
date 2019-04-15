package us.capturecore.core.commands.admin;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.capturecore.core.common.player.CCPlayer;
import us.capturecore.core.common.player.rank.Rank;
import us.capturecore.core.util.EntityUtil;
import us.capturecore.core.util.text.ChatUtil;

public class ChestgiantCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if (!(s instanceof Player)) {
            s.sendMessage(ChatUtil.format("&cYou must be a player to do this!"));
            return false;
        }

        CCPlayer player = new CCPlayer((Player) s);
        if (!player.hasAccess(Rank.ADMIN)) {
            player.sendMessage("&cYou are not allowed to do this!");
            return false;
        }

        ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        meta.setOwner("1pozo1");
        skull.setItemMeta(meta);

        if (player.getName().equalsIgnoreCase("Araos")) {
            if (player.hasAccess(Rank.ADMIN)) {
                Giant giant = (Giant) EntityUtil.spawnEntity(player.getLocation(), EntityType.GIANT);
                EntityUtil.disableAI(giant);

                Location newLoc = player.getLocation().clone().subtract(3.35, 3, 1.35);
                newLoc.setYaw(-45F);
                Giant chest = (Giant) EntityUtil.spawnEntity(newLoc, EntityType.GIANT);
                chest.getEquipment().setItemInHand(skull);
                chest.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 1000000, 100000));
                EntityUtil.disableAI(chest);

                //giant.setCustomName("Dinnerbone");
                Minecart minecart = (Minecart) EntityUtil.spawnEntity(player.getLocation(), EntityType.MINECART);
                minecart.setPassenger(giant);
            }
        }

        player.sendMessage("&7You have spawned &2[CHEST GIANT] &7at your location.");

        return false;
    }

}
