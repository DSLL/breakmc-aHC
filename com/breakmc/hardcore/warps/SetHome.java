package com.breakmc.hardcore.warps;

import com.breakmc.hardcore.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.breakmc.hardcore.spawnprot.*;
import org.bukkit.*;
import org.bukkit.configuration.file.*;

public class SetHome implements CommandExecutor
{
    private YAMLBuilder builder;
    
    public SetHome() {
        super();
        this.builder = YAMLBuilder.getInstance();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player p = (Player)sender;
        final FileConfiguration homeConf = this.builder.getWarpData();
        if (p.getLocation().getX() < SpawnProt.radius && p.getLocation().getX() > -SpawnProt.radius && p.getLocation().getZ() < SpawnProt.radius && p.getLocation().getZ() > -SpawnProt.radius) {
            p.sendMessage(ChatColor.RED + "You cannot set home within 512 blocks of spawn!");
            return true;
        }
        if (this.builder.getWarpData().getConfigurationSection("homes") == null) {
            homeConf.set("homes." + p.getName() + ".world", (Object)p.getWorld().getName());
            homeConf.set("homes." + p.getName() + ".x", (Object)p.getLocation().getX());
            homeConf.set("homes." + p.getName() + ".y", (Object)p.getLocation().getY());
            homeConf.set("homes." + p.getName() + ".z", (Object)p.getLocation().getZ());
            homeConf.set("homes." + p.getName() + ".yaw", (Object)p.getLocation().getYaw());
            homeConf.set("homes." + p.getName() + ".pitch", (Object)p.getLocation().getPitch());
            this.builder.saveWarpData();
            p.sendMessage(ChatColor.GRAY + "You have set your home.");
            return true;
        }
        homeConf.set("homes." + p.getName() + ".world", (Object)p.getWorld().getName());
        homeConf.set("homes." + p.getName() + ".x", (Object)p.getLocation().getX());
        homeConf.set("homes." + p.getName() + ".y", (Object)p.getLocation().getY());
        homeConf.set("homes." + p.getName() + ".z", (Object)p.getLocation().getZ());
        homeConf.set("homes." + p.getName() + ".yaw", (Object)p.getLocation().getYaw());
        homeConf.set("homes." + p.getName() + ".pitch", (Object)p.getLocation().getPitch());
        this.builder.saveWarpData();
        p.sendMessage(ChatColor.GRAY + "You have set your home.");
        return true;
    }
}
