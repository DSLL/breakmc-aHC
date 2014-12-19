package com.breakmc.hardcore.misc;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class MobLimit implements Listener
{
    @EventHandler
    public void limitMobs(final CreatureSpawnEvent e) {
        if (!e.getSpawnReason().equals((Object)CreatureSpawnEvent.SpawnReason.BREEDING)) {
            if (e.getEntityType().equals((Object)EntityType.SHEEP) && e.getLocation().getWorld().getEntitiesByClass((Class)Sheep.class).size() >= 100) {
                e.setCancelled(true);
            }
            else if (e.getEntityType().equals((Object)EntityType.COW) && e.getLocation().getWorld().getEntitiesByClass((Class)Cow.class).size() >= 100) {
                e.setCancelled(true);
            }
            else if (e.getEntityType().equals((Object)EntityType.CHICKEN) && e.getLocation().getWorld().getEntitiesByClass((Class)Chicken.class).size() >= 100) {
                e.setCancelled(true);
            }
            else if (e.getEntityType().equals((Object)EntityType.PIG) && e.getLocation().getWorld().getEntitiesByClass((Class)Pig.class).size() >= 100) {
                e.setCancelled(true);
            }
        }
    }
}
