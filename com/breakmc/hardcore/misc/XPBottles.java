package com.breakmc.hardcore.misc;

import org.bukkit.inventory.meta.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;
import org.bukkit.entity.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.*;

public class XPBottles implements Listener
{
    @Deprecated
    public int xpFromLevel(final int currentLevel) {
        if (currentLevel >= 30) {
            return 62 + (currentLevel - 30) * 7;
        }
        if (currentLevel >= 15) {
            return 17 + (currentLevel - 15) * 3;
        }
        return 17;
    }
    
    public static int levelToExp(final int level) {
        if (level <= 15) {
            return 17 * level;
        }
        if (level <= 30) {
            return 3 * level * level / 2 - 59 * level / 2 + 360;
        }
        return 7 * level * level / 2 - 303 * level / 2 + 2220;
    }
    
    public static ItemStack xpBottles() {
        final ItemStack xpBottles = new ItemStack(Material.EXP_BOTTLE);
        final ItemMeta meta = xpBottles.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "XP Bottle");
        xpBottles.setItemMeta(meta);
        return xpBottles;
    }
    
    public static void createRecipes() {
        final ShapelessRecipe xpBottle = new ShapelessRecipe(xpBottles()).addIngredient(1, Material.GLASS_BOTTLE);
        Bukkit.getServer().addRecipe((Recipe)xpBottle);
    }
    
    public void removeRecipes() {
        Bukkit.getServer().clearRecipes();
    }
    
    @EventHandler
    public void onPlayerCraft(final CraftItemEvent event) {
        final Player p = (Player)event.getWhoClicked();
        if (event.getCurrentItem().equals((Object)xpBottles())) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
                return;
            }
            final ItemStack experiencePotion = event.getCurrentItem();
            final ItemMeta experienceMeta = experiencePotion.getItemMeta();
            final List<String> stringList = new ArrayList<String>();
            stringList.add("");
            final int xpLevel = levelToExp(p.getLevel());
            stringList.add(ChatColor.GOLD + "Exp: " + ChatColor.WHITE + xpLevel);
            experienceMeta.setLore((List)stringList);
            experiencePotion.setItemMeta(experienceMeta);
            p.setExp(0.0f);
            p.setLevel(0);
        }
    }
    
    @EventHandler
    public void onExpSplash(final PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if ((event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && p.getItemInHand().getType() == Material.EXP_BOTTLE && p.getItemInHand().hasItemMeta()) {
            final ItemMeta meta = p.getItemInHand().getItemMeta();
            if (p.getItemInHand().getItemMeta().hasLore() && meta.getDisplayName().equals(ChatColor.GOLD + "XP Bottle")) {
                event.setCancelled(true);
                final double exp = Double.parseDouble(meta.getLore().get(1).split("§f")[1]);
                if (exp < 0.0) {
                    final ItemStack temp = p.getItemInHand().clone();
                    temp.setAmount(p.getItemInHand().getAmount() - 1);
                    p.getInventory().remove(p.getItemInHand());
                    p.getInventory().addItem(new ItemStack[] { temp });
                    p.updateInventory();
                    return;
                }
                final ItemStack temp = p.getItemInHand().clone();
                temp.setAmount(p.getItemInHand().getAmount() - 1);
                p.setItemInHand((temp.getAmount() <= 0) ? new ItemStack(Material.AIR) : temp);
                p.updateInventory();
                p.giveExp((int)exp);
            }
        }
    }
    
    public CraftPlayer getPlayerAsCraftCopy(final Player p) {
        final String version = Bukkit.getServer().getClass().getName().split("\\.")[3];
        try {
            final Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            return (CraftPlayer)craftPlayerClass.cast(p);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static int deltaLevelToExp(final int level) {
        if (level <= 15) {
            return 17;
        }
        if (level <= 30) {
            return 3 * level - 31;
        }
        return 7 * level - 155;
    }
}
