package com.breakmc.hardcore.misc;

import com.breakmc.hardcore.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Warn implements CommandExecutor
{
    private Main plugin;
    
    public Warn(final Main plugin) {
        super();
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("warn")) {
            if (!(sender instanceof Player)) {
                if (args.length < 2) {
                    sender.sendMessage(ChatColor.RED + "Correct Usage: /warn [player] [message]");
                    return false;
                }
                String warnMessage = "";
                for (int x = 1; x < args.length; ++x) {
                    warnMessage = String.valueOf(warnMessage) + args[x] + " ";
                }
                final Player t = Bukkit.getServer().getPlayer(args[0]);
                if (t == null) {
                    sender.sendMessage(ChatColor.RED + "Player not found.");
                    return false;
                }
                Bukkit.getServer().broadcastMessage(ChatColor.DARK_RED + "Console" + ChatColor.GRAY + " warned " + ChatColor.WHITE + t.getName() + ChatColor.GRAY + " for: " + ChatColor.RED + warnMessage);
                return true;
            }
            else {
                final Player p = (Player)sender;
                if (!p.hasPermission(this.plugin.MOD_PERMISSION)) {
                    p.sendMessage(ChatColor.RED + "Permission Denied.");
                    return false;
                }
                if (args.length < 2) {
                    p.sendMessage(ChatColor.RED + "Correct Usage: /warn [player] [message]");
                    return false;
                }
                String warnMessage2 = "";
                for (int x2 = 1; x2 < args.length; ++x2) {
                    warnMessage2 = String.valueOf(warnMessage2) + args[x2] + " ";
                }
                final Player t2 = Bukkit.getServer().getPlayer(args[0]);
                if (t2 == null) {
                    p.sendMessage(ChatColor.RED + "Player not found.");
                    return false;
                }
                Bukkit.getServer().broadcastMessage(String.valueOf(p.getDisplayName()) + ChatColor.GRAY + " warned " + ChatColor.WHITE + t2.getName() + ChatColor.GRAY + " for: " + ChatColor.RED + warnMessage2);
                return true;
            }
        }
        else {
            if (!cmd.getName().equalsIgnoreCase("warnall")) {
                return this.onCommand(sender, cmd, label, args);
            }
            if (!(sender instanceof Player)) {
                if (args.length >= 1) {
                    String warnMessage = "";
                    for (int x = 0; x < args.length; ++x) {
                        warnMessage = String.valueOf(warnMessage) + args[x] + " ";
                    }
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "Correct Usage: /warn [player] [message]");
                return false;
            }
            else {
                final Player p = (Player)sender;
                if (!p.hasPermission(this.plugin.MOD_PERMISSION)) {
                    p.sendMessage(ChatColor.RED + "Permission Denied");
                    return false;
                }
                if (args.length >= 1) {
                    String warnMessage2 = "";
                    for (int x2 = 0; x2 < args.length; ++x2) {
                        warnMessage2 = String.valueOf(warnMessage2) + args[x2] + " ";
                    }
                    return true;
                }
                p.sendMessage(ChatColor.RED + "Correct Usage: /warn [player] [message]");
                return false;
            }
        }
    }
}
