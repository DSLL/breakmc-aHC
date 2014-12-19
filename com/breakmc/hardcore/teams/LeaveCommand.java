package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;
import java.util.*;
import com.breakmc.hardcore.spawnprot.*;

public class LeaveCommand implements TeamCommand
{
    TeamUtils utils;
    YAMLBuilder teams;
    
    public LeaveCommand() {
        super();
        this.utils = new TeamUtils();
        this.teams = YAMLBuilder.getInstance();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length != 1) {
            p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
            return;
        }
        if (!this.utils.isOnTeam((OfflinePlayer)p)) {
            p.sendMessage(ChatColor.RED + "You are not on an team.");
            return;
        }
        final HashMap<String, String> team = new HashMap<String, String>();
        team.put(p.getName(), this.utils.getPlayerTeam((OfflinePlayer)p));
        this.utils.removeMembers((OfflinePlayer)p);
        this.utils.removeManagers((OfflinePlayer)p);
        p.sendMessage(ChatColor.DARK_AQUA + "You have left the team.");
        this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has left the team!");
        this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has left the team!");
        if (this.utils.getMembers(team.get(p.getName())).isEmpty() && this.utils.getManagers(team.get(p.getName())).isEmpty()) {
            this.teams.getTeamData().set("teams." + team.get(p.getName()), (Object)null);
            p.sendRawMessage(ChatColor.DARK_AQUA + "Team has been disbanded.");
        }
        this.teams.getTeamData().set("inteam." + p.getName(), (Object)null);
        this.teams.saveTeamData();
        team.clear();
        SpawnProt.teamss.remove(p.getName());
    }
    
    @Override
    public String name() {
        return "leave";
    }
    
    @Override
    public String aliases() {
        return "l";
    }
    
    @Override
    public String usage() {
        return "leave";
    }
    
    @Override
    public String info() {
        return "Leave your current team.";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
}
