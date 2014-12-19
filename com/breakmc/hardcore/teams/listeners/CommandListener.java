package com.breakmc.hardcore.teams.listeners;

import java.util.*;
import org.bukkit.event.player.*;
import com.breakmc.hardcore.spawnprot.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class CommandListener implements Listener
{
    List<String> blockedcommands;
    List<String> blockedcommands2;
    
    public CommandListener() {
        super();
        this.blockedcommands = Arrays.asList("/t sethq", "/t setrally", "/team setrally", "/team sethq", "/go set", "/home", "/sethome", "/t rally", "/team rally", "/t hq", "/team hq", "/warp set");
        this.blockedcommands2 = Arrays.asList("/go set", "/warp set");
    }
    
    @EventHandler
    public void onBlockedCommand(final PlayerCommandPreprocessEvent e) {
        final Player p = e.getPlayer();
        if (p.getLocation().getX() < SpawnProt.radius && p.getLocation().getX() > -SpawnProt.radius && p.getLocation().getZ() < SpawnProt.radius && p.getLocation().getZ() > -SpawnProt.radius && (this.blockedcommands.contains(e.getMessage().toLowerCase()) || this.blockedcommands2.contains(e.getMessage().toLowerCase()))) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot use that command within spawn!");
        }
        if (p.getLocation().getX() < 512.0 && p.getLocation().getX() > -512.0 && p.getLocation().getZ() < 512.0 && p.getLocation().getZ() > -512.0 && this.blockedcommands2.contains(e.getMessage().toLowerCase())) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot set warps within 512 blocks of spawn.");
        }
        if (p.getLocation().getX() < 512.0 && p.getLocation().getX() > -512.0 && p.getLocation().getZ() < 512.0 && p.getLocation().getZ() > -512.0 && e.getMessage().toLowerCase().contains("/sethome")) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot set your home within 512 blocks of spawn.");
        }
        if (p.getLocation().getX() < 512.0 && p.getLocation().getX() > -512.0 && p.getLocation().getZ() < 512.0 && p.getLocation().getZ() > -512.0 && ((e.getMessage().toLowerCase().contains("/buy") && !SpawnProt.spawnProt.contains(p.getName())) || (e.getMessage().toLowerCase().contains("/sell") && !SpawnProt.spawnProt.contains(p.getName())))) {
            e.setCancelled(true);
            p.sendMessage(ChatColor.RED + "You cannot use the economy within 512 blocks of spawn.");
        }
    }
}
