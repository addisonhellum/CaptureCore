package us.capturecore.core.common.game.data;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import us.capturecore.core.Main;
import us.capturecore.core.util.LocSerialization;

import java.io.File;
import java.util.Random;

public class CCMap {

    public static File[] getAllMapFiles() {
        return new File(Main.getInstance().getDataFolder() + "/mapdata").listFiles();
    }

    public static YamlConfiguration getData(File mapFile) {
        return YamlConfiguration.loadConfiguration(mapFile);
    }

    public static CCMap randomMap() {
        Random r = new Random();
        int index = r.nextInt(getAllMapFiles().length);

        return new CCMap(getAllMapFiles()[index]);
    }

    private String name = "Prototype";
    private String creator = "CaptureCore Team";

    private int minimumPlayers = 2;
    private int maximumPlayers = 16;

    private Location lobbyLocation;

    private Location redSpawn;
    private Location blueSpawn;

    private Location redFlag;
    private Location blueFlag;

    public CCMap() {}

    public CCMap(File file) {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);

        this.name = yaml.getString("name");
        this.creator = yaml.getString("creator");

        this.minimumPlayers = yaml.getInt("settings.minimum-players");
        this.maximumPlayers = yaml.getInt("settings.maximum-players");

        this.lobbyLocation = LocSerialization.getLocationFromString(yaml.getString("spawns.lobby"));

        this.redSpawn = LocSerialization.getLocationFromString(yaml.getString("spawns.red-spawn"));
        this.blueSpawn = LocSerialization.getLocationFromString(yaml.getString("spawns.blue-spawn"));

        this.redFlag = LocSerialization.getLocationFromString(yaml.getString("flags.red-flag"));
        this.blueFlag = LocSerialization.getLocationFromString(yaml.getString("flags.blue-flag"));
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public int getMaximumPlayers() {
        return maximumPlayers;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }

    public Location getRedSpawn() {
        return redSpawn;
    }

    public Location getBlueSpawn() {
        return blueSpawn;
    }

    public Location getRedFlag() {
        return redFlag;
    }

    public Location getBlueFlag() {
        return blueFlag;
    }

}
