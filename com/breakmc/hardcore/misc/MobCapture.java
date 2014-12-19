package com.breakmc.hardcore.misc;

import org.bukkit.event.entity.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.event.*;

public class MobCapture implements Listener
{
    @EventHandler
    public void onCap(final EntityDamageByEntityEvent e) {
        if (!e.isCancelled() && e.getDamager() instanceof Egg) {
            final Egg egg = (Egg)e.getDamager();
            final Player egg2 = (Player)egg.getShooter();
            final SpawnEggType spawnEggType = SpawnEggType.getByEntityType(e.getEntity().getType());
            if (spawnEggType == null) {
                egg2.sendMessage(ChatColor.RED + "You cannot capture this type of mob!");
                return;
            }
            if (egg2.getLevel() < spawnEggType.getCost() && !e.getEntityType().equals((Object)EntityType.MUSHROOM_COW)) {
                egg2.sendMessage(ChatColor.RED + "You need " + spawnEggType.getCost() + " xp levels to capture this mob!");
            }
            else {
                if (e.getEntity() instanceof Ageable) {
                    final Ageable ageable = (Ageable)e.getEntity();
                    if (!ageable.isAdult()) {
                        return;
                    }
                }
                if (e.getEntity() instanceof Villager) {
                    final Villager v = (Villager)e.getEntity();
                    if (CombatLog.items.containsKey(v)) {
                        return;
                    }
                }
                if (egg2.getLevel() >= 25 && (e.getEntity() instanceof Monster || (e.getEntity() instanceof Animals && !e.getEntity().getType().equals((Object)EntityType.MUSHROOM_COW) && !e.getEntityType().equals((Object)EntityType.VILLAGER)))) {
                    e.getEntity().remove();
                    final Location location = e.getEntity().getLocation();
                    final World world = location.getWorld();
                    final ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short)spawnEggType.getId());
                    world.dropItem(location, item);
                    egg2.sendMessage(ChatColor.RED + "Capturing this '" + spawnEggType.getName() + "' has drained 0 xp levels from you.");
                    return;
                }
                if (e.getEntity().getType().equals((Object)EntityType.MUSHROOM_COW)) {
                    final Random rand = new Random();
                    if (egg2.getLevel() < 25) {
                        egg2.setLevel(0);
                        egg2.setExp(0.0f);
                        egg2.sendMessage("§cFailed to capture this mob!");
                        return;
                    }
                    final int chance = (egg2.getLevel() == 25) ? 50 : ((egg2.getLevel() >= 35) ? 100 : ((egg2.getLevel() - 25) * 5));
                    if (chance == 100) {
                        e.getEntity().remove();
                        final Location location2 = e.getEntity().getLocation();
                        final World world2 = location2.getWorld();
                        final ItemStack item2 = new ItemStack(Material.MONSTER_EGG, 1, (short)spawnEggType.getId());
                        world2.dropItem(location2, item2);
                        egg2.setLevel(0);
                        egg2.setExp(0.0f);
                        egg2.sendMessage(ChatColor.RED + "Capturing this '" + spawnEggType.getName() + "' has drained all your xp levels from you.");
                        return;
                    }
                    final int realchance = (chance == 50) ? 50 : (100 / chance);
                    System.out.println(new StringBuilder().append(realchance).toString());
                    final boolean success = rand.nextInt(realchance) == 0;
                    if (!success) {
                        egg2.setLevel(0);
                        egg2.setExp(0.0f);
                        egg2.sendMessage("§cFailed to capture this mob!");
                        return;
                    }
                    egg2.setLevel(0);
                    egg2.setExp(0.0f);
                    egg2.sendMessage(ChatColor.RED + "Capture success!");
                    e.getEntity().remove();
                    final Location location3 = e.getEntity().getLocation();
                    final World world3 = location3.getWorld();
                    egg2.sendMessage(new StringBuilder(String.valueOf(realchance)).toString());
                    final ItemStack item3 = new ItemStack(Material.MONSTER_EGG, 1, (short)spawnEggType.getId());
                    world3.dropItem(location3, item3);
                }
                else {
                    e.getEntity().remove();
                    final Location location = e.getEntity().getLocation();
                    final World world = location.getWorld();
                    final ItemStack item = new ItemStack(Material.MONSTER_EGG, 1, (short)spawnEggType.getId());
                    world.dropItem(location, item);
                    egg2.setLevel(egg2.getLevel() - spawnEggType.getCost());
                    egg2.sendMessage(ChatColor.RED + "Capturing this '" + spawnEggType.getName() + "' has drained " + spawnEggType.getCost() + " xp levels from you.");
                }
            }
        }
    }
}
