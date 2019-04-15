package us.capturecore.core.util;

import org.bukkit.Location;

public class LocationUtil {

    public static double getDistanceBetween(Location loc1, Location loc2) {
        return loc1.distance(loc2);
    }

    public static boolean isWithinRange(Location loc1, Location loc2, double radius) {
        return getDistanceBetween(loc1, loc2) <= radius;
    }

}
