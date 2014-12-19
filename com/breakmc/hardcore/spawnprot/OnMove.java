package com.breakmc.hardcore.spawnprot;

import com.breakmc.hardcore.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class OnMove implements Listener
{
    int radius;
    
    public OnMove() {
        super();
        this.radius = ((Main)Main.getPlugin((Class)Main.class)).getConfig().getInt("spawn-radius");
    }
    
    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (SpawnProt.spawnProt.contains(p.getName()) && p.getWorld().getName().equals("world") && (p.getLocation().getX() > this.radius || p.getLocation().getX() < -this.radius || p.getLocation().getZ() > this.radius || p.getLocation().getZ() < -this.radius)) {
            p.sendMessage(ChatColor.GRAY + "You have lost spawn protection!");
            SpawnProt.spawnProt.remove(p.getName());
        }
    }
}
