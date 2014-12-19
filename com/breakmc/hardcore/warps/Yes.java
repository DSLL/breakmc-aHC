package com.breakmc.hardcore.warps;

import com.breakmc.hardcore.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.*;

public class Yes implements CommandExecutor
{
    private Main main;
    
    public Yes(final Main main) {
        super();
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String lbl, final String[] args) {
        final Player p = (Player)sender;
        if (Main.overridingWarp.containsKey(p.getName())) {
            final File f = this.main.getWarpFile(p.getName());
            if (!f.exists()) {
                try {
                    f.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
            final String warp = "warps." + p.getName() + "." + Main.overridingWarp.get(p.getName());
            try {
                final TempLocation tempLocation = (TempLocation)p.getMetadata("temploc").get(0).value();
                final Location locc = tempLocation.getLoc();
                builder.set(String.valueOf(warp) + ".world", (Object)locc.getWorld().getName());
                builder.set(String.valueOf(warp) + ".x", (Object)locc.getX());
                builder.set(String.valueOf(warp) + ".y", (Object)locc.getY());
                builder.set(String.valueOf(warp) + ".z", (Object)locc.getZ());
                builder.set(String.valueOf(warp) + ".yaw", (Object)locc.getYaw());
                builder.set(String.valueOf(warp) + ".pitch", (Object)locc.getPitch());
                try {
                    builder.save(f);
                }
                catch (IOException ex) {}
                p.sendRawMessage(ChatColor.GRAY + "Successfully overwrote warp '" + Main.overridingWarp.get(p.getName()) + "'.");
                p.removeMetadata("temploc", (Plugin)JavaPlugin.getPlugin((Class)Main.class));
                Main.overridingWarp.remove(p.getName());
                Bukkit.getScheduler().cancelTask(WarpMethods.taskID);
            }
            catch (NullPointerException e2) {
                p.sendRawMessage(ChatColor.RED + "This warp does not seem to exist, if you believe this is an error contact an admin!");
                Bukkit.getScheduler().cancelTask(WarpMethods.taskID);
            }
            return true;
        }
        p.sendRawMessage(ChatColor.RED + "No warp to overwrite!");
        return true;
    }
}
