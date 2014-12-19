package com.breakmc.hardcore.misc;

import org.bukkit.command.*;
import org.bukkit.*;

public class GetUUID implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        if (args.length == 0 || args.length > 1) {
            sender.sendMessage(ChatColor.RED + "/getuuid [uuid/name]");
            return true;
        }
        try {
            final OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            sender.sendMessage(ChatColor.GREEN + "UUID: " + ChatColor.WHITE + p.getUniqueId());
            return true;
        }
        catch (NullPointerException ex) {
            return false;
        }
    }
}
