package com.breakmc.hardcore.spawnprot;

import com.breakmc.hardcore.teams.*;
import com.breakmc.hardcore.*;
import org.bukkit.block.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.command.*;
import org.bukkit.plugin.java.*;
import org.bukkit.plugin.*;
import java.util.*;

public class SpawnProt implements CommandExecutor, Listener
{
    public static ArrayList<String> spawnProt;
    public static ArrayList<String> dontmove;
    public static HashMap<UUID, Boolean> hasAttacked;
    public static HashMap<String, String> teamss;
    static int thread;
    public static int radius;
    TeamUtils team;
    YAMLBuilder teams;
    private Main plugin;
    
    static {
        SpawnProt.spawnProt = new ArrayList<String>();
        SpawnProt.dontmove = new ArrayList<String>();
        SpawnProt.hasAttacked = new HashMap<UUID, Boolean>();
        SpawnProt.teamss = new HashMap<String, String>();
        SpawnProt.radius = ((Main)Main.getPlugin((Class)Main.class)).getConfig().getInt("spawn-radius");
    }
    
    public SpawnProt(final Main plugin) {
        super();
        this.team = new TeamUtils();
        this.teams = YAMLBuilder.getInstance();
        this.plugin = plugin;
    }
    
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final Player p = e.getPlayer();
        if (!p.hasPlayedBefore()) {
            p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 92.0, 0.5));
            SpawnProt.spawnProt.add(p.getName());
            return;
        }
        if (SpawnProt.spawnProt.contains(p.getName()) && p.getLocation().getX() > SpawnProt.radius && p.getLocation().getX() < -SpawnProt.radius && p.getLocation().getZ() > SpawnProt.radius && p.getLocation().getZ() < -SpawnProt.radius) {
            p.sendMessage(ChatColor.GRAY + "You have lost spawn protection.");
            SpawnProt.spawnProt.remove(p.getName());
            return;
        }
        if (!SpawnProt.spawnProt.contains(p.getName()) && p.getLocation().getX() < SpawnProt.radius && p.getLocation().getX() > -SpawnProt.radius && p.getLocation().getZ() < SpawnProt.radius && p.getLocation().getZ() > -SpawnProt.radius && !SpawnProt.hasAttacked.containsKey(p.getUniqueId())) {
            p.sendMessage(ChatColor.GRAY + "You have regained spawn protection.");
            SpawnProt.spawnProt.add(p.getName());
            return;
        }
        if (this.team.getPlayerTeam((OfflinePlayer)p) != null) {
            SpawnProt.teamss.put(p.getName(), this.team.getPlayerTeam((OfflinePlayer)p));
        }
    }
    
    @EventHandler
    public void onPlayerBreakBlock(final BlockBreakEvent event) {
        final Player p = event.getPlayer();
        final Block eBlok = event.getBlock();
        if (!p.hasPermission("adv.build") && p.getWorld().getName().equals("world") && eBlok.getX() < SpawnProt.radius && eBlok.getX() > -SpawnProt.radius && eBlok.getZ() < SpawnProt.radius && eBlok.getZ() > -SpawnProt.radius) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onPlayerPlaceBlock(final BlockPlaceEvent event) {
        final Player p = event.getPlayer();
        final Block eBlok = event.getBlock();
        if (!p.hasPermission("adv.build") && p.getWorld().getName().equals("world") && eBlok.getX() < SpawnProt.radius && eBlok.getX() > -SpawnProt.radius && eBlok.getZ() < SpawnProt.radius && eBlok.getZ() > -SpawnProt.radius) {
            event.setCancelled(true);
        }
        if (eBlok.getX() < 150 && eBlok.getX() > -150 && eBlok.getZ() < 150 && eBlok.getZ() > -150 && eBlok.getY() >= 88 && !p.isOp()) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onCreatureSpawn(final CreatureSpawnEvent event) {
        final Entity e = (Entity)event.getEntity();
        final Block eBlok = e.getLocation().getBlock();
        if (e.getWorld().getName().equals("world") && eBlok.getX() < 200 && eBlok.getX() > -200 && eBlok.getZ() < 200 && eBlok.getZ() > -200) {
            if (!(event.getEntity() instanceof Villager)) {
                event.setCancelled(true);
            }
            else if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.EGG) {
                event.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOW)
    public void onEntityDamageEvent(final EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player attacked = (Player)e.getEntity();
            if (e.getCause().equals((Object)EntityDamageEvent.DamageCause.ENTITY_ATTACK) && e.getDamager() instanceof Player) {
                final Player attacker = (Player)e.getDamager();
                if (SpawnProt.spawnProt.contains(attacked.getName()) && attacker.getWorld().getName().equals("world")) {
                    e.setCancelled(true);
                    attacker.sendMessage(ChatColor.RED + "This player has spawn protection!");
                    return;
                }
                if (SpawnProt.spawnProt.contains(attacker.getName()) && !SpawnProt.spawnProt.contains(attacked.getName()) && attacker.getWorld().getName().equals("world")) {
                    attacker.sendMessage(ChatColor.GRAY + "You have lost spawn protection!");
                    SpawnProt.spawnProt.remove(attacker.getName());
                    return;
                }
                if (SpawnProt.teamss.get(attacker.getName()) != null && SpawnProt.teamss.get(attacked.getName()) != null && SpawnProt.teamss.get(attacker.getName()).equals(SpawnProt.teamss.get(attacked.getName())) && !this.team.FriendlyFire(SpawnProt.teamss.get(attacker.getName()), attacker)) {
                    e.setCancelled(true);
                    return;
                }
            }
            if (e.getCause().equals((Object)EntityDamageEvent.DamageCause.PROJECTILE) && e.getDamager() instanceof Arrow) {
                final Arrow a = (Arrow)e.getDamager();
                if (a.getShooter() instanceof Player) {
                    final Player attacker2 = (Player)a.getShooter();
                    if (SpawnProt.spawnProt.contains(attacked.getName()) && attacker2.getWorld().getName().equals("world")) {
                        e.setCancelled(true);
                        attacker2.sendMessage(ChatColor.RED + "This player has spawn protection!");
                        return;
                    }
                    if (SpawnProt.spawnProt.contains(attacker2.getName()) && !SpawnProt.spawnProt.contains(attacked) && attacker2.getWorld().getName().equals("world")) {
                        attacker2.sendMessage(ChatColor.GRAY + "You have lost spawn protection!");
                        SpawnProt.spawnProt.remove(attacker2.getName());
                        return;
                    }
                    if (SpawnProt.teamss.get(attacker2.getName()) != null && SpawnProt.teamss.get(attacked.getName()) != null && SpawnProt.teamss.get(attacker2.getName()).equals(SpawnProt.teamss.get(attacked.getName())) && !this.team.FriendlyFire(SpawnProt.teamss.get(attacker2.getName()), attacker2)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player p = (Player)e.getEntity();
            if (p.getWorld().getName().equals("world") && SpawnProt.spawnProt.contains(p.getName())) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onRespawn(final PlayerRespawnEvent e) {
        final Player p = e.getPlayer();
        if (p.getBedSpawnLocation() != null) {
            e.setRespawnLocation(p.getBedSpawnLocation());
            return;
        }
        e.setRespawnLocation(new Location(Bukkit.getWorld("world"), 0.5, 92.0, 0.5));
        if (SpawnProt.spawnProt.contains(p.getName())) {
            SpawnProt.spawnProt.remove(p.getName());
            SpawnProt.hasAttacked.remove(p.getUniqueId());
            SpawnProt.spawnProt.add(p.getName());
            p.sendMessage(ChatColor.GRAY + "You have regained spawn protection!");
        }
        else {
            SpawnProt.spawnProt.add(p.getName());
            SpawnProt.hasAttacked.remove(p.getUniqueId());
            p.sendMessage(ChatColor.GRAY + "You have regained spawn protection!");
        }
    }
    
    @EventHandler
    public void onFoodLevelChange(final FoodLevelChangeEvent e) {
        final Player p = (Player)e.getEntity();
        if (p.getWorld().getName().equals("world") && SpawnProt.spawnProt.contains(p.getName()) && p.getLocation().getX() < SpawnProt.radius && p.getLocation().getX() > -SpawnProt.radius && p.getLocation().getZ() < SpawnProt.radius && p.getLocation().getZ() > -SpawnProt.radius && p.getFoodLevel() >= 20) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onLiquidFlow(final BlockFromToEvent e) {
        final Block b = e.getBlock();
        if (b.getWorld().getEnvironment().equals((Object)World.Environment.NORMAL) && (e.getBlock().getType() == Material.WATER || e.getBlock().getType() == Material.STATIONARY_WATER || e.getBlock().getType() == Material.LAVA || e.getBlock().getType() == Material.STATIONARY_LAVA) && b.getX() < 150 && b.getX() > -150 && b.getZ() < 150 && b.getZ() > -150) {
            e.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onEnderChestClick(final PlayerInteractEvent e) {
        final Player p = e.getPlayer();
        if (!SpawnProt.spawnProt.contains(p.getName()) && p.getLocation().getX() < SpawnProt.radius && p.getLocation().getX() > -SpawnProt.radius && p.getLocation().getZ() < SpawnProt.radius && p.getLocation().getZ() > -SpawnProt.radius && e.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && e.getClickedBlock().getType() == Material.ENDER_CHEST) {
            p.sendMessage(ChatColor.RED + "You cannot access that without spawn protection!");
            e.setCancelled(true);
        }
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        final Player p = (Player)sender;
        if (label.equalsIgnoreCase("spawn")) {
            boolean instantwarp = true;
            for (final Entity e : p.getNearbyEntities(32.0, 32.0, 32.0)) {
                if (e instanceof Player) {
                    final Player pp = (Player)e;
                    if (p.getUniqueId().equals(pp.getUniqueId())) {
                        continue;
                    }
                    if (!this.team.isOnTeam((OfflinePlayer)p)) {
                        instantwarp = false;
                        break;
                    }
                    if (!this.team.isOnTeam((OfflinePlayer)pp)) {
                        instantwarp = false;
                        break;
                    }
                    if (this.team.isOnTeam((OfflinePlayer)pp) && this.team.isOnTeam((OfflinePlayer)p) && !this.team.getPlayerTeam((OfflinePlayer)p).equalsIgnoreCase(this.team.getPlayerTeam((OfflinePlayer)pp))) {
                        instantwarp = false;
                        break;
                    }
                    continue;
                }
            }
            if (instantwarp) {
                SpawnProt.spawnProt.add(p.getName());
                System.out.println("Added player " + p.getName() + " from spawn protection!");
                p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 92.0, 0.5));
                p.sendMessage(ChatColor.GRAY + "You have regained spawn protection!");
                return true;
            }
            if (!instantwarp) {
                Main.dontmove.add(p.getName());
                p.sendMessage(ChatColor.GRAY + "Someone is nearby! Please wait 10 seconds to warp! Don't move or warping will be cancelled!");
                SpawnProt.thread = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)JavaPlugin.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (Main.dontmove.contains(p.getName())) {
                            SpawnProt.spawnProt.add(p.getName());
                            Main.dontmove.remove(p.getName());
                            p.teleport(new Location(Bukkit.getWorld("world"), 0.5, 92.0, 0.5));
                            p.sendMessage(ChatColor.GRAY + "You have regained spawn protection!");
                        }
                    }
                }, 200L);
                return true;
            }
        }
        return false;
    }
}
