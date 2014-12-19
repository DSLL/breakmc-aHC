package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;
import java.util.*;
import com.breakmc.hardcore.spawnprot.*;

public class KickCommand implements TeamCommand
{
    TeamUtils utils;
    YAMLBuilder teams;
    
    public KickCommand() {
        super();
        this.utils = new TeamUtils();
        this.teams = YAMLBuilder.getInstance();
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
            final OfflinePlayer op2 = Bukkit.getOfflinePlayer(args[1]);
            final HashMap<String, String> team = new HashMap<String, String>();
            team.put(op2.getName(), this.utils.getPlayerTeam(op2));
            team.put(p.getName(), this.utils.getPlayerTeam((OfflinePlayer)p));
            if (!this.utils.getPlayerTeam((OfflinePlayer)p).equalsIgnoreCase(this.utils.getPlayerTeam(op2))) {
                p.sendMessage(ChatColor.RED + "That player is not on your team!");
                return;
            }
            if (this.utils.isManager(op2)) {
                this.utils.removeManagers(op2);
            }
            else {
                this.utils.removeMembers(op2);
            }
            SpawnProt.teamss.remove(op2.getName());
            team.clear();
            this.teams.getTeamData().set("inteam." + p.getName(), (Object)null);
            this.teams.saveTeamData();
            p.sendMessage(ChatColor.DARK_AQUA + "You have kicked '" + args[1] + "' from the team!");
        }
        else {
            if (!this.utils.isOnTeam((OfflinePlayer)p2)) {
                p.sendMessage(ChatColor.RED + "That player is not a team!");
                return;
            }
            final HashMap<String, String> team2 = new HashMap<String, String>();
            team2.put(p2.getName(), this.utils.getPlayerTeam((OfflinePlayer)p2));
            team2.put(p.getName(), this.utils.getPlayerTeam((OfflinePlayer)p));
            if (!this.utils.getPlayerTeam((OfflinePlayer)p).equalsIgnoreCase(this.utils.getPlayerTeam((OfflinePlayer)p2))) {
                p.sendMessage(ChatColor.RED + "That player is not on your team!");
                return;
            }
            if (this.utils.isManager((OfflinePlayer)p2)) {
                this.utils.removeManagers((OfflinePlayer)p2);
            }
            else {
                this.utils.removeMembers((OfflinePlayer)p2);
            }
            SpawnProt.teamss.remove(p2.getName());
            if (this.utils.getMembers(team2.get(p.getName())).isEmpty() && this.utils.getManagers(team2.get(p.getName())).isEmpty()) {
                this.teams.getTeamData().set("teams." + team2.get(p.getName()), (Object)null);
            }
            team2.clear();
            this.teams.getTeamData().set("inteam." + p.getName(), (Object)null);
            this.teams.saveTeamData();
            p.sendMessage(ChatColor.DARK_AQUA + "You have kicked '" + args[1] + "' from the team!");
            p2.sendMessage(ChatColor.DARK_AQUA + "You have been kicked from the team!");
        }
    }
    
    @Override
    public String name() {
        return "kick";
    }
    
    @Override
    public String aliases() {
        return "k";
    }
    
    @Override
    public String usage() {
        return "kick §7(§3playerName§7)";
    }
    
    @Override
    public String info() {
        return "Kicks a player from the team.";
    }
    
    @Override
    public boolean managerOnly() {
        return true;
    }
}
