package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;

public class SetRallyCommand implements TeamCommand
{
    TeamUtils utils;
    YAMLBuilder teams;
    
    public SetRallyCommand() {
        super();
        this.utils = new TeamUtils();
        this.teams = YAMLBuilder.getInstance();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length > 1) {
            p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
            return;
        }
        if (!this.utils.isOnTeam((OfflinePlayer)p)) {
            p.sendMessage(ChatColor.RED + TeamMessageEnum.NOT_ON_TEAM.getMessage());
            return;
        }
        if (!this.utils.isManager((OfflinePlayer)p)) {
            p.sendMessage(ChatColor.RED + TeamMessageEnum.NOT_A_MANAGER.getMessage());
            return;
        }
        if (p.getLocation().getX() < 512.0 && p.getLocation().getX() > -512.0 && p.getLocation().getZ() < 512.0 && p.getLocation().getZ() > -512.0) {
            p.sendMessage(ChatColor.RED + "You must be 512 blocks away from spawn to set rally!");
            return;
        }
        if (this.teams.getTeamData().getConfigurationSection("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally") == null) {
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.world", (Object)p.getWorld().getName());
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.x", (Object)p.getLocation().getX());
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.y", (Object)p.getLocation().getY());
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.z", (Object)p.getLocation().getZ());
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.yaw", (Object)p.getLocation().getYaw());
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.pitch", (Object)p.getLocation().getPitch());
            this.teams.saveTeamData();
            this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has set the team rally point!");
            this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has set the team rally point!");
            return;
        }
        this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.world", (Object)p.getWorld().getName());
        this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.x", (Object)p.getLocation().getX());
        this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.y", (Object)p.getLocation().getY());
        this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.z", (Object)p.getLocation().getZ());
        this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.yaw", (Object)p.getLocation().getYaw());
        this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally.pitch", (Object)p.getLocation().getPitch());
        this.teams.saveTeamData();
        this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has set the team rally point!");
        this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has set the team rally point!");
    }
    
    @Override
    public String name() {
        return "setrally";
    }
    
    @Override
    public String aliases() {
        return null;
    }
    
    @Override
    public String usage() {
        return "setrally";
    }
    
    @Override
    public String info() {
        return "Sets the Teams Rally point.";
    }
    
    @Override
    public boolean managerOnly() {
        return true;
    }
}
