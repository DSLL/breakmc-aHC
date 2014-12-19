package com.breakmc.hardcore.misc;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import com.breakmc.hardcore.spawnprot.*;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.plugin.*;

public class SpawnReset implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (p.hasPermission("spawn.reset")) {
            p.sendMessage(ChatColor.RED + "Still in development :(");
            p.sendMessage(ChatColor.GREEN + "Clearing spawn...");
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)Main.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    for (int x = SpawnProt.radius; x <= 150; ++x) {
                        for (int y = 69; y <= 256; ++y) {
                            for (int z = SpawnProt.radius; z <= 150; ++z) {
                                final Block b = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), (double)x, (double)y, (double)z));
                                b.setType(Material.AIR);
                            }
                        }
                    }
                    for (int nx = -SpawnProt.radius; nx >= -150; --nx) {
                        for (int y = 69; y <= 256; ++y) {
                            for (int nz = -SpawnProt.radius; nz >= -150; --nz) {
                                final Block b = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), (double)nx, (double)y, (double)nz));
                                b.setType(Material.AIR);
                            }
                        }
                    }
                    for (int xo = SpawnProt.radius; xo <= 150; ++xo) {
                        for (int y = 69; y <= 256; ++y) {
                            final Block b2 = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), (double)xo, (double)y, 0.0));
                            b2.setType(Material.AIR);
                        }
                    }
                    for (int zo = SpawnProt.radius; zo <= 150; ++zo) {
                        for (int y = 69; y <= 256; ++y) {
                            final Block b2 = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), 0.0, (double)y, (double)zo));
                            b2.setType(Material.AIR);
                        }
                    }
                    for (int nzo = -SpawnProt.radius; nzo >= -150; --nzo) {
                        for (int y = 69; y <= 256; ++y) {
                            final Block b2 = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), 0.0, (double)y, (double)nzo));
                            b2.setType(Material.AIR);
                        }
                    }
                    for (int nxo = -SpawnProt.radius; nxo >= -150; --nxo) {
                        for (int y = 69; y <= 256; ++y) {
                            int z = 0;
                            final Block b = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), (double)nxo, (double)y, (double)z));
                            ++z;
                            b.setType(Material.AIR);
                        }
                    }
                    for (int px = SpawnProt.radius; px <= 150; ++px) {
                        for (int y = 69; y <= 256; ++y) {
                            for (int nz = -SpawnProt.radius; nz >= -150; --nz) {
                                final Block b = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), (double)px, (double)y, (double)nz));
                                b.setType(Material.AIR);
                            }
                        }
                    }
                    for (int nx = -SpawnProt.radius; nx >= -150; --nx) {
                        for (int y = 69; y <= 256; ++y) {
                            for (int pz = SpawnProt.radius; pz <= 150; ++pz) {
                                final Block b = Bukkit.getWorld("world").getBlockAt(new Location(p.getWorld(), (double)nx, (double)y, (double)pz));
                                b.setType(Material.AIR);
                            }
                        }
                    }
                }
            });
            p.sendMessage(ChatColor.GREEN + "Spawn has been cleared.");
            return false;
        }
        return true;
    }
}
