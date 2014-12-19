package com.breakmc.hardcore.misc;

import com.breakmc.hardcore.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;

public class StaffChat implements CommandExecutor, Listener
{
    private Main plugin;
    
    public StaffChat(final Main plugin) {
        super();
        this.plugin = plugin;
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!cmd.getName().equalsIgnoreCase("sc") && !cmd.getName().equalsIgnoreCase("schat") && !cmd.getName().equalsIgnoreCase("staffchat")) {
            return this.onCommand(sender, cmd, label, args);
        }
        if (!(sender instanceof Player)) {
            if (args.length >= 1) {
                String acMsg = "";
                for (int a = 0; a < args.length; ++a) {
                    acMsg = String.valueOf(acMsg) + args[a] + " ";
                }
                Bukkit.broadcast(ChatColor.AQUA + "(STAFF) Console:" + acMsg, "abundle.mod");
                return true;
            }
            sender.sendMessage(Main.red + "Correct Usage: /sc [message]");
            return false;
        }
        else {
            final Player p = (Player)sender;
            if (!p.hasPermission(this.plugin.MOD_PERMISSION)) {
                p.sendMessage(ChatColor.RED + "You are not a staff member!");
                return false;
            }
            if (args.length >= 1) {
                String acMsg2 = "";
                for (int a2 = 0; a2 < args.length; ++a2) {
                    acMsg2 = String.valueOf(acMsg2) + args[a2] + " ";
                }
                Bukkit.broadcast(ChatColor.AQUA + "(STAFF) " + p.getName() + ": " + Main.white + acMsg2, "abundle.mod");
                return true;
            }
            if (this.plugin.staffChatLock.contains(p.getUniqueId())) {
                this.plugin.staffChatLock.remove(p.getUniqueId());
                p.sendMessage(ChatColor.GRAY + "You are now talking in " + ChatColor.GOLD + "main" + ChatColor.GRAY + " chat.");
                return true;
            }
            this.plugin.staffChatLock.add(p.getUniqueId());
            p.sendMessage(ChatColor.GRAY + "You are now always talking in " + ChatColor.GOLD + "staff" + ChatColor.GRAY + " chat.");
            return true;
        }
    }
    
    @EventHandler
    public void onStaffLockChat(final AsyncPlayerChatEvent event) {
        final Player p = event.getPlayer();
        final String pMsg = event.getMessage();
        if (this.plugin.staffChatLock.contains(p.getUniqueId())) {
            event.setCancelled(true);
            Bukkit.broadcast(ChatColor.AQUA + "(STAFF) " + p.getName() + ": " + Main.white + pMsg, "abundle.mod");
        }
    }
}
