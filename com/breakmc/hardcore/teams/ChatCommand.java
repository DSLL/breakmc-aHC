package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;

public class ChatCommand implements TeamCommand
{
    TeamUtils utils;
    
    public ChatCommand() {
        super();
        this.utils = new TeamUtils();
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
        if (Main.tchat.get(p.getName()) == null) {
            Main.tchat.put(p.getName(), true);
            p.sendMessage(ChatColor.DARK_AQUA + "Now talking in team chat.");
            return;
        }
        if (Main.tchat.get(p.getName())) {
            Main.tchat.remove(p.getName());
            p.sendMessage(ChatColor.DARK_AQUA + "Now talking in public chat.");
        }
    }
    
    @Override
    public String name() {
        return "chat";
    }
    
    @Override
    public String aliases() {
        return "c";
    }
    
    @Override
    public String usage() {
        return "chat";
    }
    
    @Override
    public String info() {
        return "Toggle team chat mode.";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
}
