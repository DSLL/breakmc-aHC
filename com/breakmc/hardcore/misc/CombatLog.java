package com.breakmc.hardcore.misc;

import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.event.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.entity.*;

public class CombatLog implements Listener
{
    public static HashMap<Villager, ItemStack[]> items;
    private HashMap<Villager, String> logged;
    private HashMap<String, Villager> loggedVillager;
    private ArrayList<String> killed;
    private HashMap<String, Integer> inCombat;
    private ArrayList<String> stillAlive;
    private ArrayList<String> hasLogged;
    
    static {
        CombatLog.items = new HashMap<Villager, ItemStack[]>();
    }
    
    public CombatLog() {
        super();
        this.logged = new HashMap<Villager, String>();
        this.loggedVillager = new HashMap<String, Villager>();
        this.killed = new ArrayList<String>();
        this.inCombat = new HashMap<String, Integer>();
        this.stillAlive = new ArrayList<String>();
        this.hasLogged = new ArrayList<String>();
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final World w = p.getWorld();
        final Location loc = p.getLocation();
        final Villager villager = (Villager)w.spawnEntity(p.getLocation(), EntityType.VILLAGER);
        villager.setAdult();
        villager.setHealth(20.0);
        final String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "Logger" + ChatColor.GRAY + "] " + ChatColor.RESET;
        villager.setCustomName(String.valueOf(prefix) + p.getName());
        villager.setCustomNameVisible(true);
        villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10));
        final ItemStack[] armor = new ItemStack[4];
        final ItemStack[] contents = new ItemStack[36];
        for (int i = 0; i < p.getInventory().getSize(); ++i) {
            contents[i] = p.getInventory().getContents()[i];
        }
        for (int i = 0; i < 4; ++i) {
            armor[i] = p.getInventory().getArmorContents()[i];
        }
        final ItemStack[] fullInv = this.concat(armor, contents);
        CombatLog.items.put(villager, fullInv);
        this.logged.put(villager, p.getName());
        this.loggedVillager.put(p.getName(), villager);
    }
    
    @EventHandler
    public void onVillagerDeath(final EntityDeathEvent e) {
        final Entity ent = (Entity)e.getEntity();
        if (ent instanceof Villager) {
            final Villager villager = (Villager)ent;
            if (CombatLog.items.containsKey(villager)) {
                final ItemStack[] inv = CombatLog.items.get(villager);
                e.getDrops().addAll(Arrays.asList(inv));
                this.killed.add(this.logged.get(villager));
                CombatLog.items.remove(villager);
            }
        }
    }
    
    ItemStack[] concat(final ItemStack[] A, final ItemStack[] B) {
        final int aLen = A.length;
        final int bLen = B.length;
        final ItemStack[] C = new ItemStack[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager) {
            final Villager v = (Villager)e.getRightClicked();
            if (CombatLog.items.containsKey(v)) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (this.killed.contains(e.getPlayer().getName())) {
            e.getPlayer().getInventory().clear();
            e.getPlayer().setHealth(0.0);
            e.getPlayer().sendMessage("You have been killed while you were offline!");
            this.killed.remove(e.getPlayer().getName());
        }
        else {
            if (!this.loggedVillager.containsKey(e.getPlayer().getName())) {
                return;
            }
            if (this.loggedVillager == null) {
                return;
            }
            this.loggedVillager.get(e.getPlayer().getName()).remove();
        }
    }
    
    @EventHandler
    public void onEntityHit(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Villager) {
            final String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "Logger" + ChatColor.GRAY + "] " + ChatColor.RESET;
            final Villager villager = (Villager)e.getEntity();
            if (villager.getCustomName().contains(prefix)) {
                e.setCancelled(true);
            }
        }
    }
}
