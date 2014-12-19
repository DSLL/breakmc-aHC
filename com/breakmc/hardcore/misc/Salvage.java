package com.breakmc.hardcore.misc;

import java.util.*;
import org.bukkit.block.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;

public class Salvage implements Listener
{
    int ChestMax;
    int LegsMax;
    int HelmMax;
    int bootsMax;
    int ironMax;
    int pick;
    int swordMax;
    int hoe;
    int spade;
    int axe;
    int barding;
    private List<Material> armorMaterials;
    
    public Salvage() {
        super();
        this.ChestMax = 8;
        this.LegsMax = 7;
        this.HelmMax = 5;
        this.bootsMax = 4;
        this.ironMax = 155;
        this.pick = 3;
        this.swordMax = 2;
        this.hoe = 2;
        this.spade = 1;
        this.axe = 2;
        this.barding = 3;
        this.armorMaterials = new ArrayList<Material>(Arrays.asList(Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_HOE, Material.IRON_SPADE, Material.IRON_AXE, Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_HOE, Material.DIAMOND_SPADE, Material.DIAMOND_AXE, Material.LEATHER_BOOTS, Material.LEATHER_LEGGINGS, Material.LEATHER_CHESTPLATE, Material.LEATHER_HELMET, Material.IRON_BOOTS, Material.IRON_CHESTPLATE, Material.IRON_HELMET, Material.IRON_LEGGINGS, Material.IRON_BARDING, Material.CHAINMAIL_BOOTS, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_LEGGINGS, Material.GOLD_BOOTS, Material.GOLD_CHESTPLATE, Material.GOLD_HELMET, Material.GOLD_LEGGINGS, Material.GOLD_BARDING, Material.DIAMOND_BOOTS, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_HELMET, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BARDING, Material.GOLD_AXE, Material.GOLD_PICKAXE, Material.GOLD_HOE, Material.GOLD_SPADE, Material.GOLD_SWORD));
    }
    
    public boolean BlockNear(final Material mat, final World w, final Block block, final int x, final int y, final int z) {
        final Location loc = new Location(w, (double)(block.getX() + x), (double)(block.getY() + y), (double)(block.getZ() + z));
        final Block block2 = w.getBlockAt(loc);
        return block2.getType() == mat;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void Diamond(final PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && block.getType() == Material.DIAMOND_BLOCK && this.armorMaterials.contains(event.getPlayer().getItemInHand().getType())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
            final Player pl = event.getPlayer();
            if (this.BlockNear(Material.FURNACE, pl.getWorld(), block, -1, 0, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 1, 0, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 0, -1) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 0, 1) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, -1, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 1, 0)) {
                final ItemStack item = pl.getItemInHand();
                final Material mitem = item.getType();
                System.out.println(mitem);
                final double mult = 1.0 - item.getDurability() / this.ironMax;
                double amtIron = 0.0;
                if (mitem == Material.DIAMOND_SWORD) {
                    amtIron = Math.ceil(this.swordMax * mult);
                }
                if (mitem == Material.DIAMOND_BOOTS) {
                    amtIron = Math.ceil(this.bootsMax * mult);
                }
                if (mitem == Material.DIAMOND_HELMET) {
                    amtIron = Math.ceil(this.HelmMax * mult);
                }
                if (mitem == Material.DIAMOND_LEGGINGS) {
                    amtIron = Math.ceil(this.LegsMax * mult);
                }
                if (mitem == Material.DIAMOND_CHESTPLATE) {
                    amtIron = Math.ceil(this.ChestMax * mult);
                }
                if (mitem == Material.DIAMOND_HOE) {
                    amtIron = Math.ceil(this.hoe * mult);
                }
                if (mitem == Material.DIAMOND_AXE) {
                    amtIron = Math.ceil(this.axe * mult);
                }
                if (mitem == Material.DIAMOND_PICKAXE) {
                    amtIron = Math.ceil(this.pick);
                }
                if (mitem == Material.DIAMOND_SPADE) {
                    amtIron = Math.ceil(this.spade);
                }
                if (mitem == Material.DIAMOND_BARDING) {
                    amtIron = Math.ceil(this.barding);
                }
                if (amtIron > 0.0) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    final Inventory inv = (Inventory)pl.getInventory();
                    inv.removeItem(new ItemStack[] { item });
                    pl.getWorld().dropItem(block.getLocation(), new ItemStack(Material.DIAMOND, (int)amtIron));
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void Iron(final PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && block.getType() == Material.IRON_BLOCK && this.armorMaterials.contains(event.getPlayer().getItemInHand().getType())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
            final Player pl = event.getPlayer();
            if (this.BlockNear(Material.FURNACE, pl.getWorld(), block, -1, 0, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 1, 0, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 0, -1) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 0, 1) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, -1, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 1, 0)) {
                final ItemStack item = pl.getItemInHand();
                final Material mitem = item.getType();
                System.out.println(mitem);
                final double mult = 1.0 - item.getDurability() / this.ironMax;
                double amtIron = 0.0;
                if (mitem == Material.IRON_BOOTS) {
                    amtIron = Math.ceil(this.bootsMax * mult);
                }
                if (mitem == Material.IRON_HELMET) {
                    amtIron = Math.ceil(this.HelmMax * mult);
                }
                if (mitem == Material.IRON_LEGGINGS) {
                    amtIron = Math.ceil(this.LegsMax * mult);
                }
                if (mitem == Material.IRON_CHESTPLATE) {
                    amtIron = Math.ceil(this.ChestMax * mult);
                }
                if (mitem == Material.IRON_SWORD) {
                    amtIron = Math.ceil(this.swordMax * mult);
                }
                if (mitem == Material.IRON_HOE) {
                    amtIron = Math.ceil(this.hoe * mult);
                }
                if (mitem == Material.IRON_AXE) {
                    amtIron = Math.ceil(this.axe * mult);
                }
                if (mitem == Material.IRON_PICKAXE) {
                    amtIron = Math.ceil(this.pick);
                }
                if (mitem == Material.IRON_SPADE) {
                    amtIron = Math.ceil(this.spade);
                }
                if (mitem == Material.IRON_BARDING) {
                    amtIron = Math.ceil(this.barding);
                }
                if (amtIron > 0.0) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    final Inventory inv = (Inventory)pl.getInventory();
                    inv.removeItem(new ItemStack[] { item });
                    pl.getWorld().dropItem(block.getLocation(), new ItemStack(Material.IRON_INGOT, (int)amtIron));
                }
            }
        }
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void Gold(final PlayerInteractEvent event) {
        if (!event.hasBlock()) {
            return;
        }
        if (event.getClickedBlock() == null) {
            return;
        }
        final Block block = event.getClickedBlock();
        if (event.getAction().equals((Object)Action.RIGHT_CLICK_BLOCK) && block.getType() == Material.GOLD_BLOCK && this.armorMaterials.contains(event.getPlayer().getItemInHand().getType())) {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
            final Player pl = event.getPlayer();
            if (this.BlockNear(Material.FURNACE, pl.getWorld(), block, -1, 0, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 1, 0, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 0, -1) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 0, 1) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, -1, 0) || this.BlockNear(Material.FURNACE, pl.getWorld(), block, 0, 1, 0)) {
                final ItemStack item = pl.getItemInHand();
                final Material mitem = item.getType();
                System.out.println(mitem);
                final double mult = 1.0 - item.getDurability() / this.ironMax;
                double amtIron = 0.0;
                if (mitem == Material.GOLD_BOOTS) {
                    amtIron = Math.ceil(this.bootsMax * mult);
                }
                if (mitem == Material.GOLD_HELMET) {
                    amtIron = Math.ceil(this.HelmMax * mult);
                }
                if (mitem == Material.GOLD_LEGGINGS) {
                    amtIron = Math.ceil(this.LegsMax * mult);
                }
                if (mitem == Material.GOLD_CHESTPLATE) {
                    amtIron = Math.ceil(this.ChestMax * mult);
                }
                if (mitem == Material.GOLD_SWORD) {
                    amtIron = Math.ceil(this.swordMax * mult);
                }
                if (mitem == Material.GOLD_HOE) {
                    amtIron = Math.ceil(this.hoe * mult);
                }
                if (mitem == Material.GOLD_AXE) {
                    amtIron = Math.ceil(this.axe * mult);
                }
                if (mitem == Material.GOLD_PICKAXE) {
                    amtIron = Math.ceil(this.pick);
                }
                if (mitem == Material.GOLD_SPADE) {
                    amtIron = Math.ceil(this.spade);
                }
                if (mitem == Material.GOLD_BARDING) {
                    amtIron = Math.ceil(this.barding);
                }
                if (amtIron > 0.0) {
                    event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_USE, 1.0f, 1.0f);
                    final Inventory inv = (Inventory)pl.getInventory();
                    inv.removeItem(new ItemStack[] { item });
                    pl.getWorld().dropItem(block.getLocation(), new ItemStack(Material.GOLD_INGOT, (int)amtIron));
                }
            }
        }
    }
}
