package com.breakmc.hardcore.misc;

import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.*;
import com.breakmc.hardcore.*;
import org.bukkit.plugin.*;

public class CantAttack implements Listener
{
    private static List<String> cantattack;
    
    static {
        CantAttack.cantattack = new ArrayList<String>();
    }
    
    @EventHandler
    public void onAttack(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player && CantAttack.cantattack.contains(((Player)event.getDamager()).getName())) {
            event.setCancelled(true);
        }
    }
    
    public static void add(final Player player) {
        if (CantAttack.cantattack.contains(player.getName())) {
            CantAttack.cantattack.remove(player.getName());
        }
        CantAttack.cantattack.add(player.getName());
        Bukkit.getScheduler().runTaskLater((Plugin)Main.getPlugin((Class)Main.class), (Runnable)new Runnable() {
            @Override
            public void run() {
                CantAttack.cantattack.remove(player.getName());
            }
        }, 200L);
    }
}
