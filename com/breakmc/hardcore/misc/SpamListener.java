package com.breakmc.hardcore.misc;

import java.util.*;
import org.bukkit.event.player.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class SpamListener implements Listener
{
    private static Map<UUID, String> lastmessage;
    
    static {
        SpamListener.lastmessage = new HashMap<UUID, String>();
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        if (SpamListener.lastmessage.containsKey(e.getPlayer().getUniqueId())) {
            if (SpamListener.lastmessage.get(e.getPlayer().getUniqueId()).equalsIgnoreCase(e.getMessage())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(ChatColor.RED + "Don't repeat yourself!");
            }
            SpamListener.lastmessage.remove(e.getPlayer().getUniqueId());
        }
        SpamListener.lastmessage.put(e.getPlayer().getUniqueId(), e.getMessage());
    }
}
