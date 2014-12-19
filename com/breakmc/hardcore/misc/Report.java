package com.breakmc.hardcore.misc;

import com.breakmc.hardcore.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Report implements CommandExecutor
{
    public Main plugin;
    
    public Report(final Main plugin) {
        super();
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You do not need to report someone!");
            return false;
        }
        final Player p = (Player)sender;
        if (this.plugin.reportCooldown.containsKey(p.getUniqueId())) {
            final int reportingCooldown = 60;
            final long secondsLeft = this.plugin.reportCooldown.get(p.getUniqueId()) / 1000L + reportingCooldown - System.currentTimeMillis() / 1000L;
            if (secondsLeft > 0L) {
                p.sendMessage(Main.gray + "Please wait " + Main.green + secondsLeft + Main.gray + " seconds before doing this again.");
                return true;
            }
        }
        if (args.length < 2) {
            p.sendMessage(Main.red + "Correct Usage: /report [name] [reason]");
            return false;
        }
        final Player t = Bukkit.getServer().getPlayer(args[0]);
        if (t == null) {
            p.sendMessage(Main.red + "Player specified could not be found.");
            return false;
        }
        String reportMsg = "";
        for (int r = 1; r < args.length; ++r) {
            reportMsg = String.valueOf(reportMsg) + args[r] + " ";
        }
        Bukkit.broadcast(Main.red + "(REPORT) " + p.getName() + " says " + t.getName() + " is: " + reportMsg, "abundle.mod");
        this.plugin.reportCooldown.put(p.getUniqueId(), System.currentTimeMillis());
        p.sendMessage(Main.gray + "Player " + t.getName() + " has been reported.");
        return true;
    }
}
