package com.breakmc.hardcore.warps;

import com.breakmc.hardcore.teams.*;
import org.bukkit.command.*;
import com.breakmc.hardcore.spawnprot.*;
import org.bukkit.entity.*;
import org.bukkit.configuration.file.*;
import java.util.*;
import com.breakmc.hardcore.*;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import com.breakmc.hardcore.misc.*;
import org.bukkit.plugin.*;

public class Home implements CommandExecutor
{
    private YAMLBuilder builder;
    private TeamUtils utils;
    int warpRunnable;
    
    public Home() {
        super();
        this.builder = YAMLBuilder.getInstance();
        this.utils = new TeamUtils();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (p.getLocation().getX() < SpawnProt.radius && p.getLocation().getX() > -SpawnProt.radius && p.getLocation().getZ() < SpawnProt.radius && p.getLocation().getZ() > -SpawnProt.radius) {
            p.sendMessage(ChatColor.RED + "You cannot /home within 512 blocks of spawn!");
            return true;
        }
        if (this.builder.getWarpData().getConfigurationSection("homes") == null || this.builder.getWarpData().getConfigurationSection("homes." + p.getName()) == null) {
            p.sendMessage(ChatColor.RED + "Your home is not set!");
            return true;
        }
        final FileConfiguration homeConf = this.builder.getWarpData();
        final World world = Bukkit.getWorld(homeConf.getString("homes." + p.getName() + ".world"));
        final double x = homeConf.getDouble("homes." + p.getName() + ".x");
        final double y = homeConf.getDouble("homes." + p.getName() + ".y");
        final double z = homeConf.getDouble("homes." + p.getName() + ".z");
        final float yaw = (float)homeConf.getDouble("homes." + p.getName() + ".yaw");
        final float pitch = (float)homeConf.getDouble("homes." + p.getName() + ".pitch");
        for (final Entity e : p.getNearbyEntities(16.0, 32.0, 16.0)) {
            if (e instanceof Player) {
                final Player pp = (Player)e;
                if (p.getName().equalsIgnoreCase(pp.getName())) {
                    continue;
                }
                if (!this.utils.isOnTeam((OfflinePlayer)p)) {
                    this.teleport(p, "home", false, world, x, y, z, pitch, yaw);
                    return true;
                }
                if (!this.utils.isOnTeam((OfflinePlayer)pp)) {
                    this.teleport(p, "home", false, world, x, y, z, pitch, yaw);
                    return true;
                }
                if (this.utils.isOnTeam((OfflinePlayer)pp) && this.utils.isOnTeam((OfflinePlayer)p) && !this.utils.getPlayerTeam((OfflinePlayer)p).equalsIgnoreCase(this.utils.getPlayerTeam((OfflinePlayer)pp))) {
                    this.teleport(p, "home", false, world, x, y, z, pitch, yaw);
                    return true;
                }
                continue;
            }
        }
        this.teleport(p, "home", true, world, x, y, z, pitch, yaw);
        return false;
    }
    
    public void teleport(final Player p, final String name, final boolean instawarp, final World world, final double x, final double y, final double z, final float pitch, final float yaw) {
        if (!instawarp) {
            p.sendMessage(ChatColor.GRAY + "Someone is nearby! Warping in 10 seconds! Don't move!");
            Main.dontmove.add(p.getName());
            this.warpRunnable = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)JavaPlugin.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (Main.dontmove.contains(p.getName())) {
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        CantAttack.add(p);
                        p.sendMessage(ChatColor.GRAY + "You cannot attack for 10 seconds!");
                        Main.dontmove.remove(p.getName());
                    }
                }
            }, 200L);
            return;
        }
        p.teleport(new Location(world, x, y, z, yaw, pitch));
        CantAttack.add(p);
        p.sendMessage(ChatColor.GRAY + "You cannot attack for 10 seconds!");
    }
}
