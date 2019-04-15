package us.capturecore.core.util.text;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import us.capturecore.core.Main;
import us.capturecore.core.event.ServerStartEvent;

import java.lang.reflect.Field;

public class TabTitleManager implements Listener {

    public static void sendPlayerListTab(Player player, String header, String footer) {
        CraftPlayer craftplayer = (CraftPlayer)player;
        PlayerConnection connection =
                craftplayer.getHandle().playerConnection;
        IChatBaseComponent hj = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + header + "\"}"));
        IChatBaseComponent fj = IChatBaseComponent.ChatSerializer.a(ChatColor.translateAlternateColorCodes('&', "{\"text\": \"" + footer + "\"}"));
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
        try
        {
            Field headerField = packet.getClass().getDeclaredField("a");
            headerField.setAccessible(true);
            headerField.set(packet, hj);
            headerField.setAccessible(!headerField.isAccessible());

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, fj);
            footerField.setAccessible(!footerField.isAccessible());
        }
        catch (Exception localException) {}
        connection.sendPacket(packet);
    }

    @EventHandler
    public void onStart(ServerStartEvent event) {
        new BukkitRunnable() {
            int index = 0;
            String[] frames = new String[] {
                "&3Discord @ &b&ldiscord.capturecore.us", "&eDonate @ &6&lstore.capturecore.us"
            };

            @Override
            public void run() {
                if (index >= frames.length) index = 0;
                String frame = frames[index];

                for (Player player : Bukkit.getOnlinePlayers())
                    sendPlayerListTab(player, "&f&m+------&r  &9&lCAPTURE&c&lCORE &f&m------+", frame);

                index++;
            }
        }.runTaskTimer(Main.getInstance(), 20, 100);
    }

}