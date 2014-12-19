package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;

public class RallyCommand implements TeamCommand
{
    TeamUtils utils;
    YAMLBuilder teams;
    
    public RallyCommand() {
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
        if (this.teams.getTeamData().getConfigurationSection("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".rally") == null) {
            p.sendMessage(ChatColor.RED + "The teams rally is not set!");
            return;
        }
        this.utils.warpRally(p);
    }
    
    @Override
    public String name() {
        return "rally";
    }
    
    @Override
    public String aliases() {
        return null;
    }
    
    @Override
    public String usage() {
        return "rally";
    }
    
    @Override
    public String info() {
        return "Teleports you to the teams Rally point.";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
}
