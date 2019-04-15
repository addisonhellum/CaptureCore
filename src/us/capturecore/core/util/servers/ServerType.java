package us.capturecore.core.util.servers;

import org.bukkit.configuration.file.FileConfiguration;
import us.capturecore.core.Main;

public enum ServerType {

    LOBBY, GAMEMODE, EVENT;

    public static ServerType getServerType() {
        FileConfiguration config = Main.getInstance().getConfig();
        String type = config.getString("server-type");

        if (type.equalsIgnoreCase("lobby")) return ServerType.LOBBY;
        if (type.equalsIgnoreCase("game")) return ServerType.GAMEMODE;
        if (type.equalsIgnoreCase("event")) return ServerType.EVENT;
        return ServerType.LOBBY;
    }

}
