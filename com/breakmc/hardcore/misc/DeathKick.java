package com.breakmc.hardcore.misc;

import java.util.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import com.breakmc.hardcore.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;

public class DeathKick implements Listener
{
    HashMap<String, Long> deathbanned;
    HashMap<String, Long> deathbannedPing;
    
    public DeathKick() {
        super();
        this.deathbanned = new HashMap<String, Long>();
        this.deathbannedPing = new HashMap<String, Long>();
    }
    
    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        final Player p = e.getEntity();
        if (!p.hasPermission("deathkick.bypass")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)Main.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (p.isDead()) {
                        DeathKick.this.deathbanned.put(p.getName(), System.currentTimeMillis());
                        DeathKick.this.deathbannedPing.put(p.getAddress().getAddress().getHostAddress(), System.currentTimeMillis());
                        p.kickPlayer("You have died!\nYou may log back in after §b60 §rseconds");
                    }
                }
            }, 5L);
        }
    }
    
    @EventHandler
    public void onDeathBannedLogin(final AsyncPlayerPreLoginEvent e) {
        final long now = System.currentTimeMillis();
        final Long lastChat = this.deathbanned.get(e.getName());
        if (lastChat != null) {
            final long earliestNext = lastChat + 60000L;
            if (now < earliestNext) {
                final int timeRemaining = (int)((earliestNext - now) / 1000L) + 1;
                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You may log back in after §b" + timeRemaining + "§r seconds.");
            }
        }
    }
    
    @EventHandler
    public void onDeathBanPing(final ServerListPingEvent e) throws Exception {
        final long now = System.currentTimeMillis();
        final Long lastChat = this.deathbannedPing.get(e.getAddress().getHostAddress());
        if (lastChat != null) {
            final long earliestNext = lastChat + 60000L;
            if (now < earliestNext) {
                final int timeRemaining = (int)((earliestNext - now) / 1000L) + 1;
                e.setMotd("§fYou may log back in after §b" + timeRemaining + " §fseconds.");
            }
        }
    }
}
