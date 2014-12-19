package com.breakmc.hardcore.teams.listeners;

import com.breakmc.hardcore.teams.*;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.*;
import org.bukkit.*;
import com.breakmc.hardcore.spawnprot.*;
import com.breakmc.hardcore.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;

public class JoinListener implements Listener
{
    TeamUtils utils;
    
    public JoinListener() {
        super();
        this.utils = new TeamUtils();
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        e.setJoinMessage((String)null);
        e.getPlayer().sendMessage("§cLoading your data...");
        new BukkitRunnable() {
            public void run() {
                if (JoinListener.this.utils.isOnTeam((OfflinePlayer)e.getPlayer()) && !SpawnProt.teamss.containsKey(e.getPlayer().getName())) {
                    SpawnProt.teamss.put(e.getPlayer().getName(), JoinListener.this.utils.getPlayerTeam((OfflinePlayer)e.getPlayer()));
                }
            }
        }.runTaskAsynchronously((Plugin)Main.getPlugin((Class)Main.class));
        e.getPlayer().sendMessage("§aDone.");
    }
}
