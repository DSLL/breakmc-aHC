package com.breakmc.hardcore.warps;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class WarpAdmin implements CommandExecutor
{
    WarpMethods warps;
    
    public WarpAdmin() {
        super();
        this.warps = WarpMethods.getInstance();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (cmd.getName().equalsIgnoreCase("goas")) {
            if (args.length == 0 || args.length > 2) {
                p.sendMessage(ChatColor.RED + "/" + label + " <playername> [warpname]");
                return true;
            }
            if (args.length == 1) {
                final Player t = Bukkit.getPlayerExact(args[0]);
                if (t == null) {
                    final OfflinePlayer to = Bukkit.getOfflinePlayer(args[0]);
                    this.warps.listWarpsAdmin(p, args[0]);
                    return true;
                }
                this.warps.listWarpsAdmin(p, t);
                return true;
            }
            else if (args.length == 2) {
                final Player t = Bukkit.getPlayerExact(args[0]);
                if (t == null) {
                    final OfflinePlayer to = Bukkit.getOfflinePlayer(args[0]);
                    this.warps.warpAdmin(p, args[1], to);
                    return true;
                }
                this.warps.warpAdmin(p, args[1], t);
                return true;
            }
        }
        else if (cmd.getName().equalsIgnoreCase("godelas")) {
            if (args.length != 2) {
                p.sendMessage(ChatColor.RED + "/" + label + " <playername> [warpname]");
                return true;
            }
            final Player t = Bukkit.getPlayerExact(args[0]);
            if (t == null) {
                final OfflinePlayer to = Bukkit.getOfflinePlayer(args[0]);
                this.warps.deleteWarpAs(to, p, args[1]);
                return true;
            }
            this.warps.deleteWarpAs((OfflinePlayer)t, p, args[1]);
            return true;
        }
        return false;
    }
}
