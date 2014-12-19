package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import org.bukkit.*;
import com.breakmc.hardcore.*;

public class RosterCommand implements TeamCommand
{
    TeamUtils utils;
    YAMLBuilder builder;
    
    public RosterCommand() {
        super();
        this.utils = new TeamUtils();
        this.builder = YAMLBuilder.getInstance();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length == 1 || args.length > 2) {
            p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
            return;
        }
        if (this.utils.matchTeamName(args[1]) == null) {
            p.sendMessage(ChatColor.RED + "Team \"" + args[1] + "\" does not exist.");
            return;
        }
        p.sendMessage(ChatColor.GRAY + "***" + ChatColor.DARK_AQUA + this.utils.matchTeamName(args[1]) + ChatColor.GRAY + "***");
        if (p.hasPermission("teams.admin")) {
            p.sendMessage(ChatColor.GRAY + (this.builder.getTeamData().getString("teams." + this.utils.matchTeamName(args[1]) + ".password").isEmpty() ? "Password: Not Set" : ("Password: " + this.builder.getTeamData().getString("teams." + this.utils.matchTeamName(args[1]) + ".password"))));
        }
        p.sendMessage(ChatColor.GRAY + "Members:");
        this.utils.printOnlineManagers(p, this.utils.matchTeamName(args[1]), this.utils.getManagers(this.utils.matchTeamName(args[1])));
        this.utils.printOfflineManagers(p, this.utils.matchTeamName(args[1]), this.utils.getManagers(this.utils.matchTeamName(args[1])));
        this.utils.printOnlineMembers(p, this.utils.matchTeamName(args[1]), this.utils.getMembers(this.utils.matchTeamName(args[1])));
        this.utils.printOfflineMembers(p, this.utils.matchTeamName(args[1]), this.utils.getMembers(this.utils.matchTeamName(args[1])));
    }
    
    @Override
    public String name() {
        return "roster";
    }
    
    @Override
    public String usage() {
        return "roster §7(§3Team§7)";
    }
    
    @Override
    public String info() {
        return "View the members of a team.";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
    
    @Override
    public String aliases() {
        return "r";
    }
}
