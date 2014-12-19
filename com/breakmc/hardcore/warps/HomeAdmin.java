package com.breakmc.hardcore.warps;

import com.breakmc.hardcore.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.configuration.file.*;
import org.bukkit.*;

public class HomeAdmin implements CommandExecutor
{
    private YAMLBuilder builder;
    
    public HomeAdmin() {
        super();
        this.builder = YAMLBuilder.getInstance();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (!cmd.getName().equalsIgnoreCase("homeas")) {
            return false;
        }
        if (args.length == 0 || args.length > 1) {
            p.sendMessage(ChatColor.RED + "/" + label + " <playername>");
            return true;
        }
        if (args.length == 1) {
            final OfflinePlayer t = Bukkit.getOfflinePlayer(args[0]);
            if (t == null) {
                p.sendMessage(ChatColor.RED + "Player not found!");
                return true;
            }
            if (this.builder.getWarpData().getConfigurationSection("homes") == null || this.builder.getWarpData().getConfigurationSection("homes." + t.getName()) == null) {
                p.sendMessage(ChatColor.RED + "That player's home is not set!");
                return true;
            }
            final FileConfiguration homeConf = this.builder.getWarpData();
            final World world = Bukkit.getWorld(homeConf.getString("homes." + t.getName() + ".world"));
            final double x = homeConf.getDouble("homes." + t.getName() + ".x");
            final double y = homeConf.getDouble("homes." + t.getName() + ".y");
            final double z = homeConf.getDouble("homes." + t.getName() + ".z");
            final float yaw = (float)homeConf.getDouble("homes." + t.getName() + ".yaw");
            final float pitch = (float)homeConf.getDouble("homes." + t.getName() + ".pitch");
            p.teleport(new Location(world, x, y, z, yaw, pitch));
        }
        return true;
    }
}
