package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;

public class FriendlyFireCommand implements TeamCommand
{
    YAMLBuilder teams;
    TeamUtils utils;
    
    public FriendlyFireCommand() {
        super();
        this.teams = YAMLBuilder.getInstance();
        this.utils = new TeamUtils();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length == 1 || args.length > 2) {
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
        if (args[1].equalsIgnoreCase("on")) {
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".friendlyfire", (Object)true);
            this.teams.saveTeamData();
            this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has enabled friendly fire!");
            this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has enabled friendly fire!");
            return;
        }
        if (args[1].equalsIgnoreCase("off")) {
            this.teams.getTeamData().set("teams." + this.utils.getPlayerTeam((OfflinePlayer)p) + ".friendlyfire", (Object)false);
            this.teams.saveTeamData();
            this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has disabled friendly fire!");
            this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.DARK_AQUA + p.getName() + " has disabled friendly fire!");
            return;
        }
        p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
    }
    
    @Override
    public String name() {
        return "ff";
    }
    
    @Override
    public String aliases() {
        return "friendlyfire";
    }
    
    @Override
    public String usage() {
        return "ff §7(§3on§7/§3off§7)";
    }
    
    @Override
    public String info() {
        return "Toggles friendly fire on or off.";
    }
    
    @Override
    public boolean managerOnly() {
        return true;
    }
}
