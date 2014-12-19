package com.breakmc.hardcore.warps;

import com.breakmc.hardcore.teams.*;
import com.breakmc.hardcore.*;
import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.metadata.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import com.breakmc.hardcore.misc.*;

public class WarpMethods
{
    File file;
    FileConfiguration fc;
    TeamUtils utils;
    WarpManager man;
    public static int warpRunnable;
    public static int taskID;
    private static Main main;
    static WarpMethods instance;
    
    static {
        WarpMethods.warpRunnable = 0;
        WarpMethods.taskID = 0;
        WarpMethods.instance = new WarpMethods(WarpMethods.main);
    }
    
    public WarpMethods(final Main main) {
        super();
        this.utils = new TeamUtils();
        this.man = WarpManager.getInstance();
        WarpMethods.main = main;
    }
    
    public static WarpMethods getInstance() {
        return WarpMethods.instance;
    }
    
    public void createWarp(final Player p, final String name) {
        if (name.contains("==")) {
            p.sendMessage(ChatColor.RED + "You cannot set a warp with that name!");
            return;
        }
        final File f = WarpMethods.main.getWarpFile(p.getName());
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (IOException ex) {}
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + p.getName()) == null) {
            builder.set("warps." + p.getName() + "." + name + ".world", (Object)p.getLocation().getWorld().getName());
            builder.set("warps." + p.getName() + "." + name + ".x", (Object)p.getLocation().getX());
            builder.set("warps." + p.getName() + "." + name + ".y", (Object)p.getLocation().getY());
            builder.set("warps." + p.getName() + "." + name + ".z", (Object)p.getLocation().getZ());
            builder.set("warps." + p.getName() + "." + name + ".yaw", (Object)p.getLocation().getYaw());
            builder.set("warps." + p.getName() + "." + name + ".pitch", (Object)p.getLocation().getPitch());
            try {
                builder.save(f);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            p.sendMessage(ChatColor.GRAY + "Warp '" + name + "' has been set!");
            return;
        }
        final Set<String> warps = (Set<String>)builder.getConfigurationSection("warps." + p.getName()).getKeys(false);
        if (this.matchWarp(p, name) != null) {
            Main.overridingWarp.put(p.getName(), this.matchWarp(p, name));
            p.setMetadata("temploc", (MetadataValue)new FixedMetadataValue((Plugin)JavaPlugin.getPlugin((Class)Main.class), (Object)new TempLocation(p.getLocation())));
            p.sendMessage("§eYou already have a warp with the name '§a" + this.matchWarp(p, name) + "§e',\nif you want to overwrite it type §2/confirm,\n§eor type §c/deny §eto keep the warp.\n§eThis will expire after 10 seconds.");
            WarpMethods.taskID = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)JavaPlugin.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    p.sendRawMessage(ChatColor.RED + "You did not enter an answer! Cancelling request!");
                    Main.overridingWarp.remove(p.getName());
                }
            }, 200L);
            return;
        }
        if (warps.size() >= this.getWarpLimit(p)) {
            p.sendMessage(ChatColor.RED + "You have set the max amount of warps!");
            return;
        }
        builder.set("warps." + p.getName() + "." + name + ".world", (Object)p.getLocation().getWorld().getName());
        builder.set("warps." + p.getName() + "." + name + ".x", (Object)p.getLocation().getX());
        builder.set("warps." + p.getName() + "." + name + ".y", (Object)p.getLocation().getY());
        builder.set("warps." + p.getName() + "." + name + ".z", (Object)p.getLocation().getZ());
        builder.set("warps." + p.getName() + "." + name + ".yaw", (Object)p.getLocation().getYaw());
        builder.set("warps." + p.getName() + "." + name + ".pitch", (Object)p.getLocation().getPitch());
        try {
            builder.save(f);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        p.sendMessage(ChatColor.GRAY + "Warp '" + name + "' has been set!");
    }
    
    public void transferWarp(final UUID uuid, final String name, final Location loc) {
        final File f = WarpMethods.main.getWarpFile(uuid.toString());
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (IOException ex) {}
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + uuid.toString()) == null) {
            builder.set("warps." + uuid + "." + name + ".world", (Object)loc.getWorld().getName());
            builder.set("warps." + uuid + "." + name + ".x", (Object)loc.getX());
            builder.set("warps." + uuid + "." + name + ".y", (Object)loc.getY());
            builder.set("warps." + uuid + "." + name + ".z", (Object)loc.getZ());
            builder.set("warps." + uuid + "." + name + ".yaw", (Object)loc.getYaw());
            builder.set("warps." + uuid + "." + name + ".pitch", (Object)loc.getPitch());
            try {
                builder.save(f);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        builder.set("warps." + uuid + "." + name + ".world", (Object)loc.getWorld().getName());
        builder.set("warps." + uuid + "." + name + ".x", (Object)loc.getX());
        builder.set("warps." + uuid + "." + name + ".y", (Object)loc.getY());
        builder.set("warps." + uuid + "." + name + ".z", (Object)loc.getZ());
        builder.set("warps." + uuid + "." + name + ".yaw", (Object)loc.getYaw());
        builder.set("warps." + uuid + "." + name + ".pitch", (Object)loc.getPitch());
        try {
            builder.save(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteWarp(final Player p, final String name) {
        final File f = WarpMethods.main.getWarpFile(p.getName());
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (IOException ex) {}
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (this.matchWarp(p, name) == null) {
            p.sendMessage(ChatColor.RED + "Warp '" + name + "' does not exist!");
            return;
        }
        p.sendMessage(ChatColor.GRAY + "Warp '" + this.matchWarp(p, name) + "' has been deleted!");
        builder.set("warps." + p.getName() + "." + this.matchWarp(p, name), (Object)null);
        try {
            builder.save(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void deleteWarpAs(final OfflinePlayer p, final Player exec, final String name) {
        final File f = WarpMethods.main.getWarpFile(p.getName());
        if (!f.exists()) {
            try {
                f.createNewFile();
            }
            catch (IOException ex) {}
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (this.matchWarp(p, name) == null) {
            exec.sendMessage(ChatColor.RED + "Warp '" + name + "' does not exist!");
            return;
        }
        exec.sendMessage(ChatColor.GREEN + "Deleted " + p.getName() + "'s warp named '" + name + "'!");
        builder.set("warps." + p.getName() + "." + this.matchWarp(p, name), (Object)null);
        try {
            builder.save(f);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void listWarps(final Player p) {
        final FileConfiguration builder = WarpMethods.main.getWarpConfig(p.getName());
        if (builder.getConfigurationSection("warps." + p.getName()) == null) {
            p.sendMessage(ChatColor.GRAY + "***Warp List (0/" + this.getWarpLimit(p) + ")***");
            p.sendMessage(ChatColor.GRAY + "[]");
            return;
        }
        final Set<String> warps = (Set<String>)builder.getConfigurationSection("warps." + p.getName()).getKeys(false);
        final StringBuilder sb = new StringBuilder();
        for (final String warp : warps) {
            sb.append(warp).append(", ");
        }
        String warpList = sb.toString().trim();
        if (warpList.endsWith(",")) {
            warpList = warpList.substring(0, warpList.length() - 1);
        }
        p.sendMessage(ChatColor.GRAY + "***Warp List (" + warps.size() + "/" + this.getWarpLimit(p) + ")***");
        p.sendMessage(ChatColor.GRAY + "[" + warpList + "]");
    }
    
    public void listWarpsAdmin(final Player p, final Player t) {
        final File f = new File(((Main)JavaPlugin.getPlugin((Class)Main.class)).getDataFolder() + File.separator + "warp", String.valueOf(t.getName()) + ".yml");
        if (!f.exists()) {
            p.sendMessage(ChatColor.RED + "That player has not played on the server yet!");
            return;
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + t.getName()) == null) {
            p.sendMessage(ChatColor.GRAY + String.format("Showing warps for: %s", t.getName()));
            p.sendMessage(ChatColor.GRAY + "[]");
            return;
        }
        final Set<String> warps = (Set<String>)builder.getConfigurationSection("warps." + t.getName()).getKeys(false);
        final StringBuilder sb = new StringBuilder();
        for (final String warp : warps) {
            sb.append(warp).append(", ");
        }
        String warpList = sb.toString().trim();
        if (warpList.endsWith(",")) {
            warpList = warpList.substring(0, warpList.length() - 1);
        }
        p.sendMessage(ChatColor.GRAY + String.format("Showing warps for: %s", t.getName()));
        p.sendMessage(ChatColor.GRAY + "[" + warpList + "]");
    }
    
    public void listWarpsAdmin(final Player p, final String t) {
        if (t == null) {
            p.sendMessage(ChatColor.RED + "That player has not played on the server yet!");
            return;
        }
        final File f = new File(((Main)JavaPlugin.getPlugin((Class)Main.class)).getDataFolder() + File.separator + "warp", String.valueOf(t) + ".yml");
        if (!f.exists()) {
            p.sendMessage(ChatColor.RED + "That player has not played on the server yet!");
            return;
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + t) == null) {
            p.sendMessage(ChatColor.GRAY + String.format("Showing warps for: %s", t));
            p.sendMessage(ChatColor.GRAY + "[]");
            return;
        }
        final Set<String> warps = (Set<String>)builder.getConfigurationSection("warps." + t).getKeys(false);
        final StringBuilder sb = new StringBuilder();
        for (final String warp : warps) {
            sb.append(warp).append(", ");
        }
        String warpList = sb.toString().trim();
        if (warpList.endsWith(",")) {
            warpList = warpList.substring(0, warpList.length() - 1);
        }
        p.sendMessage(ChatColor.GRAY + String.format("Showing warps for: %s", t));
        p.sendMessage(ChatColor.GRAY + "[" + warpList + "]");
    }
    
    public void warp(final Player p, final String name) {
        final FileConfiguration builder = WarpMethods.main.getWarpConfig(p.getName());
        if (this.matchWarp(p, name) == null) {
            p.sendMessage(ChatColor.RED + "Warp '" + name + "' does not exist!");
            return;
        }
        final World world = Bukkit.getWorld(builder.getString("warps." + p.getName() + "." + this.matchWarp(p, name) + ".world"));
        final double x = builder.getDouble("warps." + p.getName() + "." + this.matchWarp(p, name) + ".x");
        final double y = builder.getDouble("warps." + p.getName() + "." + this.matchWarp(p, name) + ".y");
        final double z = builder.getDouble("warps." + p.getName() + "." + this.matchWarp(p, name) + ".z");
        final float pitch = Float.valueOf(builder.getString("warps." + p.getName() + "." + this.matchWarp(p, name) + ".pitch"));
        final float yaw = Float.valueOf(builder.getString("warps." + p.getName() + "." + this.matchWarp(p, name) + ".yaw"));
        for (final Entity e : p.getNearbyEntities(32.0, 32.0, 32.0)) {
            if (e instanceof Player) {
                final Player pp = (Player)e;
                if (p.getName().equalsIgnoreCase(pp.getName())) {
                    continue;
                }
                if (!this.utils.isOnTeam((OfflinePlayer)p)) {
                    this.teleport(p, this.matchWarp(p, name), false, world, x, y, z, pitch, yaw);
                    return;
                }
                if (!this.utils.isOnTeam((OfflinePlayer)pp)) {
                    this.teleport(p, this.matchWarp(p, name), false, world, x, y, z, pitch, yaw);
                    return;
                }
                if (this.utils.isOnTeam((OfflinePlayer)pp) && this.utils.isOnTeam((OfflinePlayer)p) && !this.utils.getPlayerTeam((OfflinePlayer)p).equalsIgnoreCase(this.utils.getPlayerTeam((OfflinePlayer)pp))) {
                    this.teleport(p, this.matchWarp(p, name), false, world, x, y, z, pitch, yaw);
                    return;
                }
                continue;
            }
        }
        this.teleport(p, this.matchWarp(p, name), true, world, x, y, z, pitch, yaw);
    }
    
    public void teleport(final Player p, final String name, final boolean instawarp, final World world, final double x, final double y, final double z, final float pitch, final float yaw) {
        if (!instawarp) {
            p.sendMessage(ChatColor.GRAY + "Someone is nearby! Warping in 10 seconds! Don't move!");
            Main.dontmove.add(p.getName());
            WarpMethods.warpRunnable = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)JavaPlugin.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (Main.dontmove.contains(p.getName())) {
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        p.sendMessage(ChatColor.GRAY + "You cannot attack for 10 seconds!");
                        CantAttack.add(p);
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
    
    public int getWarpLimit(final Player p) {
        int count = 0;
        if (p.hasPermission("warps.register")) {
            count += 3;
        }
        if (!p.hasPermission("warps.maxwarps") || !p.isOp()) {
            for (int i = 0; i < 100; ++i) {
                if (p.hasPermission("warps." + i)) {
                    count = i;
                    break;
                }
            }
        }
        else {
            count = 100;
        }
        return count;
    }
    
    public void warpAdmin(final Player p, final String name, final OfflinePlayer t) {
        final File f = WarpMethods.main.getWarpFile(t.getName());
        if (!f.exists()) {
            p.sendMessage(ChatColor.RED + "Could not find warp file for player '" + t.getName() + "'.");
            return;
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + t.getName()) == null) {
            p.sendMessage(ChatColor.RED + "This player has no warps!");
            return;
        }
        if (this.matchWarp(t, name) == null) {
            p.sendMessage(ChatColor.RED + String.format("Warp '" + name + "' does not exist for player %s!", t.getName()));
            return;
        }
        final World world = Bukkit.getWorld(builder.getString("warps." + t.getName() + "." + this.matchWarp(t, name) + ".world"));
        final double x = builder.getDouble("warps." + t.getName() + "." + this.matchWarp(t, name) + ".x");
        final double y = builder.getDouble("warps." + t.getName() + "." + this.matchWarp(t, name) + ".y");
        final double z = builder.getDouble("warps." + t.getName() + "." + this.matchWarp(t, name) + ".z");
        final float pitch = Float.valueOf(builder.getString("warps." + t.getName() + "." + this.matchWarp(t, name) + ".pitch"));
        final float yaw = Float.valueOf(builder.getString("warps." + t.getName() + "." + this.matchWarp(t, name) + ".yaw"));
        p.teleport(new Location(world, x, y, z, pitch, yaw));
        p.sendMessage(ChatColor.GRAY + "Warped to '" + this.matchWarp(t, name) + "'.");
    }
    
    public void warpAdmin(final Player p, final String name, final Player t) {
        final File f = WarpMethods.main.getWarpFile(t.getName());
        if (!f.exists()) {
            p.sendMessage(ChatColor.RED + "Could not find warp file for player '" + t.getName() + "'.");
            return;
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + t.getName()) == null) {
            p.sendMessage(ChatColor.RED + "This player has no warps!");
            return;
        }
        if (this.matchWarp(t, name) == null) {
            p.sendMessage(ChatColor.RED + String.format("Warp '" + name + "' does not exist for player %s!", t.getName()));
            return;
        }
        final World world = Bukkit.getWorld(builder.getString("warps." + t.getName() + "." + this.matchWarp(t, name) + ".world"));
        final double x = builder.getDouble("warps." + t.getName() + "." + this.matchWarp(t, name) + ".x");
        final double y = builder.getDouble("warps." + t.getName() + "." + this.matchWarp(t, name) + ".y");
        final double z = builder.getDouble("warps." + t.getName() + "." + this.matchWarp(t, name) + ".z");
        final float pitch = Float.valueOf(builder.getString("warps." + t.getName() + "." + this.matchWarp(t, name) + ".pitch"));
        final float yaw = Float.valueOf(builder.getString("warps." + t.getName() + "." + this.matchWarp(t, name) + ".yaw"));
        p.teleport(new Location(world, x, y, z, pitch, yaw));
        p.sendMessage(ChatColor.GRAY + "Warped to '" + name + "'.");
    }
    
    public boolean doesWarpExist(final Player p, final String name) {
        final File f = WarpMethods.main.getWarpFile(p.getName());
        if (!f.exists()) {
            return false;
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + p.getName()) == null) {
            return false;
        }
        for (final String s : builder.getConfigurationSection("warps." + p.getName()).getKeys(false)) {
            if (s.equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public String matchWarp(final Player p, final String name) {
        final File f = WarpMethods.main.getWarpFile(p.getName());
        if (!f.exists()) {
            return null;
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + p.getName()) == null) {
            return null;
        }
        for (final String s : builder.getConfigurationSection("warps." + p.getName()).getKeys(false)) {
            if (s.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
    
    public String matchWarp(final OfflinePlayer p, final String name) {
        final File f = WarpMethods.main.getWarpFile(p.getName());
        if (!f.exists()) {
            return null;
        }
        final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        if (builder.getConfigurationSection("warps." + p.getName()) == null) {
            return null;
        }
        for (final String s : builder.getConfigurationSection("warps." + p.getName()).getKeys(false)) {
            if (s.equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
}
