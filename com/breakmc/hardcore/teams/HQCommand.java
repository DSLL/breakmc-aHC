package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;

public class HQCommand implements TeamCommand
{
    TeamUtils utils;
    YAMLBuilder teams;
    
    public HQCommand() {
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
        if (this.teams.getTeamData().getConfigurationSection("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".hq") == null) {
            p.sendMessage(ChatColor.RED + "The teams HQ is not set!");
            return;
        }
        this.utils.warpHQ(p);
    }
    
    @Override
    public String name() {
        return "hq";
    }
    
    @Override
    public String aliases() {
        return null;
    }
    
    @Override
    public String usage() {
        return "hq";
    }
    
    @Override
    public String info() {
        return "Teleports you to the teams HQ.";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
}
