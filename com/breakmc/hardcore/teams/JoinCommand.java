package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;
import com.breakmc.hardcore.spawnprot.*;
import org.bukkit.configuration.*;

public class JoinCommand implements TeamCommand
{
    YAMLBuilder teams;
    TeamUtils utils;
    
    public JoinCommand() {
        super();
        this.teams = YAMLBuilder.getInstance();
        this.utils = new TeamUtils();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length == 1 || args.length > 3) {
            p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
        }
        if (this.utils.isOnTeam((OfflinePlayer)p)) {
            p.sendMessage(ChatColor.RED + "You are already on a team!");
            return;
        }
        if (args.length == 2) {
            final ConfigurationSection team = this.teams.getTeamData().getConfigurationSection("teams." + args[1]);
            if (team == null) {
                p.sendMessage(ChatColor.RED + "That team does not exist.");
                return;
            }
            if (p.hasPermission("teams.admin")) {
                p.sendMessage(ChatColor.DARK_AQUA + "You have successfully joined the team!");
                this.utils.addPlayerToTeam((OfflinePlayer)p, args[1]);
                this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has joined the team!");
                this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has joined the team!");
                this.teams.getTeamData().set("inteam." + p.getName(), (Object)args[1]);
                this.teams.saveTeamData();
                SpawnProt.teamss.put(p.getName(), args[1]);
                return;
            }
            if (!team.getString(".password").isEmpty()) {
                p.sendMessage(ChatColor.RED + "Team requires a password!");
                return;
            }
            this.utils.addPlayerToTeam((OfflinePlayer)p, args[1]);
            p.sendMessage(ChatColor.DARK_AQUA + "You have successfully joined the team!");
            this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has joined the team!");
            this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has joined the team!");
            this.teams.getTeamData().set("inteam." + p.getName(), (Object)args[1]);
            this.teams.saveTeamData();
            SpawnProt.teamss.put(p.getName(), args[1]);
        }
        else {
            if (args.length != 3) {
                return;
            }
            final ConfigurationSection team = this.teams.getTeamData().getConfigurationSection("teams." + args[1]);
            if (team == null) {
                p.sendMessage(ChatColor.RED + "That team does not exist.");
                return;
            }
            if (team.getString(".password").isEmpty()) {
                p.sendMessage(ChatColor.RED + "Team does not require a password!");
                return;
            }
            if (!team.getString(".password").equalsIgnoreCase(args[2])) {
                p.sendMessage(ChatColor.RED + "Incorrect password!");
                return;
            }
            this.utils.addPlayerToTeam((OfflinePlayer)p, args[1]);
            p.sendMessage(ChatColor.DARK_AQUA + "You have successfully joined the team!");
            this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has joined the team!");
            this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has joined the team!");
            this.teams.getTeamData().set("inteam." + p.getName(), (Object)args[1]);
            this.teams.saveTeamData();
            SpawnProt.teamss.put(p.getName(), args[1]);
        }
    }
    
    @Override
    public String name() {
        return "join";
    }
    
    @Override
    public String aliases() {
        return "j";
    }
    
    @Override
    public String usage() {
        return "join §7(§3Team§7) (§3password§7)";
    }
    
    @Override
    public String info() {
        return "Join a team";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
}
