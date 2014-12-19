package com.breakmc.hardcore.sql;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class Register implements CommandExecutor
{
    public boolean onCommand(final CommandSender s, final Command cmd, final String label, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("register") && s instanceof Player) {
            final Player p = (Player)s;
            if (RegisterSQL.doesDatabaseContainUuid(p.getUniqueId(), "users")) {
                p.sendMessage(ChatColor.RED + "You're already registered.");
                p.sendMessage(ChatColor.RED + "Change password with /password.");
                p.sendMessage(ChatColor.RED + "Change email with /email.");
                return false;
            }
            if (args.length != 2) {
                p.sendMessage(ChatColor.RED + "Correct Usage: /register [email] [password]");
                p.sendMessage(ChatColor.RED + "Passwords may not have spaces.");
                return false;
            }
            final String message = args[1];
            final String email = args[0];
            if (!email.contains("@") || !email.contains(".") || email.equals(" ")) {
                p.sendMessage(ChatColor.RED + "Please enter a valid email.");
                return false;
            }
            if (message.length() < 7) {
                p.sendMessage(ChatColor.RED + "The password must be at least 6 characters.");
                return false;
            }
            RegisterSQL.doInsert(p.getUniqueId(), "users", message, email);
            p.sendMessage(ChatColor.GRAY + "You have " + ChatColor.AQUA + "registered " + ChatColor.GRAY + "on the forums.");
            return true;
        }
        else if (cmd.getName().equalsIgnoreCase("password") && s instanceof Player) {
            final Player p = (Player)s;
            if (!RegisterSQL.doesDatabaseContainUuid(p.getUniqueId(), "users")) {
                p.sendMessage(ChatColor.RED + "You're not registered!");
                p.sendMessage(ChatColor.RED + "Register with /register.");
                return false;
            }
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Correct Usage: /password [new password]");
                return false;
            }
            final String message = args[0];
            if (message.length() < 7) {
                p.sendMessage(ChatColor.RED + "The password must be at least 6 characters.");
                return false;
            }
            RegisterSQL.doSet(p.getUniqueId(), "users", "password", RegisterSQL.hash(message), "uuid", p.getUniqueId().toString());
            p.sendMessage(ChatColor.GRAY + "You have " + ChatColor.AQUA + "updated " + ChatColor.GRAY + "your password.");
            return true;
        }
        else {
            if (!cmd.getName().equalsIgnoreCase("email") || !(s instanceof Player)) {
                return false;
            }
            final Player p = (Player)s;
            if (!RegisterSQL.doesDatabaseContainUuid(p.getUniqueId(), "users")) {
                p.sendMessage(ChatColor.RED + "You're not registered!");
                p.sendMessage(ChatColor.RED + "Register with /register.");
                return false;
            }
            if (args.length != 1) {
                p.sendMessage(ChatColor.RED + "Correct Usage: /email [new email]");
                return false;
            }
            final String email2 = args[0];
            if (!email2.contains("@") || !email2.contains(".") || email2.equals(" ")) {
                p.sendMessage(ChatColor.RED + "Please enter a valid email.");
                return false;
            }
            RegisterSQL.doSet(p.getUniqueId(), "users", "email", email2, "uuid", p.getUniqueId().toString());
            p.sendMessage(ChatColor.GRAY + "You have " + ChatColor.AQUA + "updated " + ChatColor.GRAY + "your email.");
            return true;
        }
    }
}
