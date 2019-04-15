package us.capturecore.core.util.text;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Title {

    private String _title;
    private String _subtitle;
    private int _fadeIn;
    private int _stay;
    private int _fadeOut;

    public Title(String title, String subtitle) {
        this._title = ChatUtil.format(title);
        this._subtitle = ChatUtil.format(subtitle);
        this._fadeIn = 0;
        this._stay = 40;
        this._fadeOut = 0;
    }

    public Title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        this._title = ChatUtil.format(title);
        this._subtitle = ChatUtil.format(subtitle);
        this._fadeIn = fadeIn;
        this._stay = stay;
        this._fadeOut = fadeOut;
    }

    public String getTitle() { return _title; }
    public String getSubtitle() { return _subtitle; }
    public int getFadeIn() { return _fadeIn; }
    public int getStay() { return _stay; }
    public int getFadeOut() { return _fadeOut; }

    public Title setTitle(String title) {
        this._title = ChatUtil.format(title);
        return this;
    }

    public Title setSubtitle(String subtitle) {
        this._subtitle = ChatUtil.format(subtitle);
        return this;
    }

    public Title setFadeIn(int ticks) {
        this._fadeIn = ticks;
        return this;
    }

    public Title setStay(int ticks) {
        this._stay = ticks;
        return this;
    }

    public Title setFadeOut(int ticks) {
        this._fadeOut = ticks;
        return this;
    }

    public void send(Player player) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;

        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null,
                getFadeIn(), getStay(), getFadeOut());
        connection.sendPacket(packetPlayOutTimes);

        if (getSubtitle() != null) {
            IChatBaseComponent titleSub = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + getSubtitle() + "\"}");
            PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, titleSub);
            connection.sendPacket(packetPlayOutSubTitle);
        }

        if (getTitle() != null) {
            IChatBaseComponent titleMain = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + getTitle() + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, titleMain);
            connection.sendPacket(packetPlayOutTitle);
        }
    }

}