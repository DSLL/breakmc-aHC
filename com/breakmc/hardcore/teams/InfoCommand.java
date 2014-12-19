package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import org.bukkit.*;

public class InfoCommand implements TeamCommand
{
    TeamUtils utils;
    YAMLBuilder teams;
    
    public InfoCommand() {
        super();
        this.utils = new TeamUtils();
        this.teams = YAMLBuilder.getInstance();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length > 2) {
            p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
            return;
        }
        if (args.length == 1) {
            if (!this.utils.isOnTeam((OfflinePlayer)p) || this.teams.getTeamData().getConfigurationSection("teams") == null) {
                p.sendMessage(ChatColor.RED + TeamMessageEnum.NOT_ON_TEAM.getMessage());
                return;
            }
            new BukkitRunnable() {
                public void run() {
                    final String hq = (InfoCommand.this.teams.getTeamData().getConfigurationSection("teams." + InfoCommand.this.utils.getPlayerTeam((OfflinePlayer)p) + ".hq") == null) ? "Team HQ: Not Set" : "Team HQ: Set";
                    final String rally = (InfoCommand.this.teams.getTeamData().getConfigurationSection("teams." + InfoCommand.this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally") == null) ? "Team Rally: Not Set" : "Team Rally: Set";
                    final String ff = InfoCommand.this.utils.FriendlyFire(p) ? "Friendly Fire is on" : "Friendly Fire is off";
                    final String pass = InfoCommand.this.teams.getTeamData().getString("teams." + InfoCommand.this.utils.getPlayerTeam((OfflinePlayer)p) + ".password").isEmpty() ? "Password: Not Set" : ("Password: " + InfoCommand.this.teams.getTeamData().getString("teams." + InfoCommand.this.utils.getPlayerTeam((OfflinePlayer)p) + ".password"));
                    final String members = "Members:";
                    p.sendMessage(ChatColor.GRAY + "***" + ChatColor.DARK_AQUA + InfoCommand.this.utils.getPlayerTeam((OfflinePlayer)p) + ChatColor.GRAY + "***");
                    p.sendMessage(ChatColor.GRAY + pass);
                    p.sendMessage(ChatColor.GRAY + hq);
                    p.sendMessage(ChatColor.GRAY + rally);
                    p.sendMessage(ChatColor.GRAY + ff);
                    p.sendRawMessage(ChatColor.GRAY + members);
                    InfoCommand.this.utils.printManagers(p, InfoCommand.this.utils.getManagers((OfflinePlayer)p));
                    InfoCommand.this.utils.printMembers(p, InfoCommand.this.utils.getMembers((OfflinePlayer)p));
                }
            }.runTaskAsynchronously((Plugin)JavaPlugin.getPlugin((Class)Main.class));
        }
        else {
            if (args.length != 2) {
                return;
            }
            final Player pi = Bukkit.getPlayer(args[1]);
            if (pi == null) {
                final OfflinePlayer pio = Bukkit.getOfflinePlayer(args[1]);
                if (!this.utils.isOnTeam(pio)) {
                    p.sendMessage(ChatColor.RED + "That player is not on any team!");
                    return;
                }
                new BukkitRunnable() {
                    public void run() {
                        final String members = "Members:";
                        p.sendMessage(ChatColor.GRAY + "***" + ChatColor.DARK_AQUA + InfoCommand.this.utils.getPlayerTeam(pio) + ChatColor.GRAY + "***");
                        if (p.hasPermission("teams.admin")) {
                            p.sendMessage(ChatColor.GRAY + (InfoCommand.this.teams.getTeamData().getString("teams." + InfoCommand.this.utils.getPlayerTeam(pio) + ".password").isEmpty() ? "Password: Not Set" : ("Password: " + InfoCommand.this.teams.getTeamData().getString("teams." + InfoCommand.this.utils.getPlayerTeam(pio) + ".password"))));
                        }
                        p.sendMessage(ChatColor.GRAY + members);
                        InfoCommand.this.utils.printManagers(p, InfoCommand.this.utils.getManagers(pio));
                        InfoCommand.this.utils.printMembers(p, InfoCommand.this.utils.getMembers(pio));
                    }
                }.runTaskAsynchronously((Plugin)JavaPlugin.getPlugin((Class)Main.class));
            }
            else {
                final String members = "Members:";
                if (!this.utils.isOnTeam((OfflinePlayer)pi)) {
                    p.sendMessage(ChatColor.RED + "That player is not on any team!");
                    return;
                }
                new BukkitRunnable() {
                    public void run() {
                        p.sendMessage(ChatColor.GRAY + "***" + ChatColor.DARK_AQUA + InfoCommand.this.utils.getPlayerTeam((OfflinePlayer)pi) + ChatColor.GRAY + "***");
                        p.sendMessage(ChatColor.GRAY + "Members:");
                        InfoCommand.this.utils.printManagers(p, InfoCommand.this.utils.getManagers((OfflinePlayer)pi));
                        InfoCommand.this.utils.printMembers(p, InfoCommand.this.utils.getMembers((OfflinePlayer)pi));
                    }
                }.runTaskAsynchronously((Plugin)JavaPlugin.getPlugin((Class)Main.class));
            }
        }
    }
    
    @Override
    public String name() {
        return "info";
    }
    
    @Override
    public String aliases() {
        return "i";
    }
    
    @Override
    public String usage() {
        return "info §7(§3playerName§7)";
    }
    
    @Override
    public String info() {
        return "Gives you information about your team.";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
}
