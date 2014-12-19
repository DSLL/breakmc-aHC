package com.breakmc.hardcore.teams.listeners;

import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class AnvilListener implements Listener
{
    @EventHandler
    public void onAnvilClick(final PlayerInteractEvent e) {
        if (e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType() == Material.ANVIL) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(ChatColor.RED + "Anvils are temporarily disabled to fix a bug. Sorry.");
        }
    }
}
