package us.capturecore.core.common.game.gameutil;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import us.capturecore.core.Main;

public class Shopkeeper {

    public enum ShopkeeperType {
        RED, BLUE;
    }

    private ShopkeeperType type;

    public Shopkeeper(ShopkeeperType type) {
        this.type = type;
    }

    public ShopkeeperType getType() {
        return type;
    }

    public String[] getFrames() {
        if (getType().equals(ShopkeeperType.RED))
            return new String[] {
                    "eyJ0aW1lc3RhbXAiOjE1MzA3MDQ4NjQ5ODUsInByb2ZpbGVJZCI6IjU1OTE5ZTMxZDEzZTQ1Mzg4ZDY0YzgwMDA5ZTI3ZjdmIiwic" +
                        "HJvZmlsZU5hbWUiOiJLYXp5S2F6b28iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0tJTiI6ey" +
                        "J1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzVhZjUyODMxNDEwZTMyZGU0NDFiYTVkZjU" +
                        "xOWE5OTAxYzExM2VmOTljMWM2OTljYjNlMTE4NWUyYWFlZTc1ZmQifX19",
                    "eyJ0aW1lc3RhbXAiOjE1MzA3MDQ5MTk3ODIsInByb2ZpbGVJZCI6IjU1OTE5ZTMxZDEzZTQ1Mzg4ZDY0YzgwMDA5ZTI3ZjdmI" +
                            "iwicHJvZmlsZU5hbWUiOiJLYXp5S2F6b28iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0" +
                            "tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNlNmRlMWFiZjY5MGNlZTB" +
                            "kY2VmNDAzNTU3N2NmYTVlYjYxMTQ4MmFkZDNkMjQ1NmNhM2I3YmM4MTBlYzZlN2QifX19"
            };
        else if (getType().equals(ShopkeeperType.BLUE))
            return new String[] {
                    "eyJ0aW1lc3RhbXAiOjE1MzA3MDQ5ODY3MDAsInByb2ZpbGVJZCI6IjU1OTE5ZTMxZDEzZTQ1Mzg4ZDY0YzgwMDA5ZTI3ZjdmI" +
                            "iwicHJvZmlsZU5hbWUiOiJLYXp5S2F6b28iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0" +
                            "tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2JmMTRkMDY2NmZiZjEyN2R" +
                            "kMTk4MjkwOGFlMjQ5ZDA5MTNiODE5ZmQyMjMyMWE3MmM4YTI2NjRlNDdlN2JlOTgifX19",
                    "eyJ0aW1lc3RhbXAiOjE1MzA3MDUwNjgxNDcsInByb2ZpbGVJZCI6IjU1OTE5ZTMxZDEzZTQ1Mzg4ZDY0YzgwMDA5ZTI3ZjdmI" +
                            "iwicHJvZmlsZU5hbWUiOiJLYXp5S2F6b28iLCJzaWduYXR1cmVSZXF1aXJlZCI6dHJ1ZSwidGV4dHVyZXMiOnsiU0" +
                            "tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzNmZDNlZGNjMjUxNTNmZGJ" +
                            "lMWY1YTQxZWM1YTE1NWI3NGJkZjFlZjlhOTYyYjg1MDZlOTEwNGRhODY4YzJlNmQifX19"
            };

        return null;
    }

    public void displayPrototype(Location location) {
        ArmorStand stand = location.getWorld().spawn(location, ArmorStand.class);
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                if (index == 60) {
                    stand.remove();
                    cancel();
                }

                int frame = index % 2;
                String value = getFrames()[frame];

                stand.setHelmet(SkullCreator.fromBase64(SkullCreator.Type.ITEM, value));

                index++;
            }
        }.runTaskTimer(Main.getInstance(), 20, 20);
    }

}
