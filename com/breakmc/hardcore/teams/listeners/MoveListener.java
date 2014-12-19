package com.breakmc.hardcore.teams.listeners;

import org.bukkit.event.player.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;
import com.breakmc.hardcore.warps.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class MoveListener implements Listener
{
    @EventHandler
    public void onDontMove(final PlayerMoveEvent e) {
        final Player p = e.getPlayer();
        if (Main.dontmove.contains(p.getName())) {
            if (e.getFrom().getBlockX() == e.getTo().getBlockX() && e.getFrom().getBlockY() == e.getTo().getBlockY()) {
                if (e.getFrom().getBlockZ() == e.getTo().getBlockZ()) {
                    return;
                }
            }
            while (Main.dontmove.contains(p.getName())) {
                Main.dontmove.remove(p.getName());
            }
            p.sendMessage(ChatColor.GRAY + "Warping cancelled!");
            Bukkit.getScheduler().cancelTask(WarpMethods.warpRunnable);
        }
        else if (!Main.dontmove.contains(p.getName())) {
            return;
        }
    }
}
