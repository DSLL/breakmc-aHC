package com.breakmc.hardcore.teams.listeners;

import com.breakmc.hardcore.teams.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import com.breakmc.hardcore.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class ChatListener implements Listener
{
    TeamUtils utils;
    
    public ChatListener() {
        super();
        this.utils = new TeamUtils();
    }
    
    @EventHandler
    public void onTeamChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        String message = e.getMessage();
        message = message.replace("<3", "\u2764§r");
        e.setMessage(message);
        if (!this.utils.isOnTeam((OfflinePlayer)p)) {
            return;
        }
        if (!Main.tchat.containsKey(p.getName())) {
            return;
        }
        final String name = this.utils.isManager((OfflinePlayer)p) ? ("§3" + p.getName()) : ("§7" + p.getName());
        e.setCancelled(true);
        this.utils.messageManagers(this.utils.getManagers((OfflinePlayer)p), "§7[" + this.utils.getPlayerTeam((OfflinePlayer)p) + "] §f§r" + name + "§f: " + message);
        this.utils.messageMembers(this.utils.getMembers((OfflinePlayer)p), "§7[" + this.utils.getPlayerTeam((OfflinePlayer)p) + "] §f§r" + name + "§f: " + message);
    }
}
