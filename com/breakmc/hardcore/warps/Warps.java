package com.breakmc.hardcore.warps;

import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.breakmc.hardcore.spawnprot.*;

public class Warps implements CommandExecutor
{
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] strings) {
        if (strings.length == 0 || strings.length > 2) {
            commandSender.sendMessage(ChatColor.RED + "Improper /" + s + " Usage! Try:\n");
            commandSender.sendMessage(ChatColor.GRAY + "/" + s + " set <name> - Creates a warp.");
            commandSender.sendMessage(ChatColor.GRAY + "/" + s + " del <name> - Deletes a warp.");
            commandSender.sendMessage(ChatColor.GRAY + "/" + s + " list - Lists your warps.");
            commandSender.sendMessage(ChatColor.GRAY + "/" + s + " <name> - Teleports you to a warp.");
            return true;
        }
        final WarpMethods warp = WarpMethods.getInstance();
        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("set")) {
                warp.createWarp((Player)commandSender, strings[1]);
                return true;
            }
            if (strings[0].equalsIgnoreCase("del") || strings[0].equalsIgnoreCase("delete")) {
                warp.deleteWarp((Player)commandSender, strings[1]);
                return true;
            }
        }
        else {
            if (strings.length != 1) {
                commandSender.sendMessage(ChatColor.RED + "Improper /" + s + " Usage! Try:\n");
                commandSender.sendMessage(ChatColor.GRAY + "/" + s + " set <name> - Creates a warp.");
                commandSender.sendMessage(ChatColor.GRAY + "/" + s + " del <name> - Deletes a warp.");
                commandSender.sendMessage(ChatColor.GRAY + "/" + s + " list - Lists your warps.");
                commandSender.sendMessage(ChatColor.GRAY + "/" + s + " <name> - Teleports you to a warp.");
                return true;
            }
            if (strings[0].equalsIgnoreCase("list")) {
                warp.listWarps((Player)commandSender);
                return true;
            }
            if (!strings[0].equalsIgnoreCase("list") && !strings[0].equalsIgnoreCase("set") && !strings[0].equalsIgnoreCase("del") && !strings[0].equalsIgnoreCase("delete")) {
                final Player p = (Player)commandSender;
                if (p.getLocation().getX() < SpawnProt.radius && p.getLocation().getX() > -SpawnProt.radius && p.getLocation().getZ() < SpawnProt.radius && p.getLocation().getZ() > -SpawnProt.radius) {
                    p.sendMessage(ChatColor.RED + "You may not warp in spawn.");
                    return true;
                }
                warp.warp(p, strings[0]);
            }
        }
        return false;
    }
}
