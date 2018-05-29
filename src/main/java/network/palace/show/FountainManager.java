package network.palace.show;

import network.palace.show.handlers.Fountain;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FountainManager implements Listener {
    public static List<UUID> blocks = new ArrayList<>();
    public List<Fountain> fountains = new ArrayList<>();

    public FountainManager() {
        start();
    }

    @SuppressWarnings("deprecation")
    private void start() {
        Bukkit.getScheduler().runTaskTimer(ShowPlugin.getInstance(), () -> {
            for (Fountain fon : new ArrayList<>(fountains)) {
                double duration = fon.getDuration();
                fon.setDuration(duration - 0.2);
                if (duration <= 0) {
                    fountains.remove(fon);
                    continue;
                }
                Location loc = fon.getLocation();
                int type = fon.getType();
                byte data = fon.getData();
                Vector force = fon.getForce();
                FallingBlock fb = loc.getWorld().spawnFallingBlock(loc, type, data);
                fb.setVelocity(force);
                blocks.add(fb.getUniqueId());
            }
        }, 0L, 4L);
    }

    @EventHandler
    public void entityToBlock(EntityChangeBlockEvent event) {
        Entity e = event.getEntity();
        if (blocks.remove(e.getUniqueId())) {
            event.setCancelled(true);
            e.remove();
        }
    }

    public void addFountain(Fountain fountain) {
        fountains.add(fountain);
    }
}