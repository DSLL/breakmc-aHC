package com.breakmc.hardcore.warps;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;

public class No implements CommandExecutor
{
    public boolean onCommand(final CommandSender sender, final Command command, final String lbl, final String[] args) {
        final Player p = (Player)sender;
        if (Main.overridingWarp.containsKey(p.getName())) {
            p.sendRawMessage(ChatColor.RED + "Request to overwrite warp " + Main.overridingWarp.get(p.getName()) + " cancelled.");
            Main.overridingWarp.remove(p.getName());
            Bukkit.getScheduler().cancelTask(WarpMethods.taskID);
            return true;
        }
        p.sendRawMessage(ChatColor.RED + "No warp to overwrite!");
        return true;
    }
}
