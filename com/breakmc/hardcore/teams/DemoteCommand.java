package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;
import java.util.*;
import com.breakmc.hardcore.spawnprot.*;

public class DemoteCommand implements TeamCommand
{
    TeamUtils utils;
    
    public DemoteCommand() {
        super();
        this.utils = new TeamUtils();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length == 1 || args.length > 2) {
            p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
            return;
        }
        if (args.length != 2) {
            return;
        }
        if (!this.utils.isOnTeam((OfflinePlayer)p)) {
            p.sendMessage(ChatColor.RED + "You are not on a team!");
            return;
        }
        if (this.utils.getMembers((OfflinePlayer)p).contains(p.getName())) {
            p.sendMessage(ChatColor.RED + "You must be a manager to perform this command!");
            return;
        }
        final Player p2 = Bukkit.getPlayer(args[1]);
        if (p2 == null) {
            p.sendMessage(ChatColor.RED + "That player is not online!");
            return;
        }
        if (!this.utils.isOnTeam((OfflinePlayer)p2)) {
            p.sendMessage(ChatColor.RED + "That player is not a team!");
            return;
        }
        final HashMap<String, String> team = new HashMap<String, String>();
        team.put(p2.getName(), this.utils.getPlayerTeam((OfflinePlayer)p2));
        team.put(p.getName(), this.utils.getPlayerTeam((OfflinePlayer)p));
        if (!team.get(p.getName()).equalsIgnoreCase(team.get(p2.getName()))) {
            p.sendMessage(ChatColor.RED + "That player is not on your team!");
            return;
        }
        if (!this.utils.isManager((OfflinePlayer)p2)) {
            p.sendMessage(ChatColor.RED + "That player is already demoted!");
            return;
        }
        this.utils.removeManagers((OfflinePlayer)p2);
        this.utils.addPlayerToTeam((OfflinePlayer)p2, team.get(p.getName()));
        SpawnProt.teamss.remove(p.getName());
        p.sendMessage(ChatColor.DARK_AQUA + "You have demoted '" + args[1] + "'!");
        p2.sendMessage(ChatColor.DARK_AQUA + "You have been demoted!");
    }
    
    @Override
    public String name() {
        return "demote";
    }
    
    @Override
    public String aliases() {
        return "d";
    }
    
    @Override
    public String usage() {
        return "demote §7(§3PlayerName§7)";
    }
    
    @Override
    public String info() {
        return "Demote a player to manager.";
    }
    
    @Override
    public boolean managerOnly() {
        return true;
    }
}
