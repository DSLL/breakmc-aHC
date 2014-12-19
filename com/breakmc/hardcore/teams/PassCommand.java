package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;

public class PassCommand implements TeamCommand
{
    TeamUtils utils;
    
    public PassCommand() {
        super();
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
        this.utils.setPassword(p, args[1]);
        this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), ChatColor.GRAY + p.getName() + " set the password to '" + args[1] + "'!");
        this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), ChatColor.GRAY + p.getName() + " set the password to '" + args[1] + "'!");
    }
    
    @Override
    public String name() {
        return "pass";
    }
    
    @Override
    public String aliases() {
        return "password";
    }
    
    @Override
    public String usage() {
        return "password §7(§3password §7| §3null §7| §3none§7)";
    }
    
    @Override
    public String info() {
        return "Changes your teams password";
    }
    
    @Override
    public boolean managerOnly() {
        return true;
    }
}
