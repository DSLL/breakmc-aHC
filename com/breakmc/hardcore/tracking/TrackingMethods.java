package com.breakmc.hardcore.tracking;

import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.*;

public class TrackingMethods
{
    int mx;
    int my;
    int mz;
    
    public void setLoc(final int x, final int y, final int z) {
        this.mx = x;
        this.my = y;
        this.mz = z;
    }
    
    public boolean checkPlayer(final Player pl, final int x, final int z) {
        int num = 0;
        if (x == 0) {
            final int plz = pl.getLocation().getBlockZ();
            num = Math.abs(z);
            if (Math.abs(this.mz - plz) < num) {
                if (z < 0) {
                    if (plz <= this.mz) {
                        return true;
                    }
                }
                else if (plz >= this.mz) {
                    return true;
                }
            }
        }
        else if (z == 0) {
            final int plz = pl.getLocation().getBlockX();
            num = Math.abs(x);
            if (Math.abs(this.mx - plz) < num) {
                if (x < 0) {
                    if (plz <= this.mx) {
                        return true;
                    }
                }
                else if (plz >= this.mx) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void TrackDir(final Player player, final int x, final int z, final Player player2) {
        String compass = "North";
        final int num = Math.abs(x) + Math.abs(z);
        final boolean can = this.checkPlayer(player2, x, z);
        if (z < 0) {
            compass = "North";
        }
        else if (z > 0) {
            compass = "South";
        }
        if (x < 0) {
            compass = "West";
        }
        else if (x > 0) {
            compass = "East";
        }
        if (player == player2) {
            player.sendMessage(String.valueOf(player2.getDisplayName()) + ChatColor.GREEN + " IS " + "within " + num + " blocks " + compass);
        }
        else {
            player.sendMessage(String.valueOf(player2.getDisplayName()) + (can ? (ChatColor.GREEN + " IS ") : (ChatColor.RED + " IS NOT ")) + "within " + num + " blocks " + compass);
        }
    }
    
    public int findBlock(final World world, final int x, final int z, final Material mat1, final Material mat2) {
        boolean hasmat = true;
        int length = 0;
        for (int i = 1; i < 1000; ++i) {
            final Block block = world.getBlockAt(this.mx + x * i, this.my, this.mz + z * i);
            final Material bmat = block.getType();
            if (hasmat) {
                if (bmat == mat1) {
                    ++length;
                    if (mat1 == Material.COBBLESTONE) {
                        block.setType(Material.AIR);
                    }
                }
                else {
                    if (bmat != mat2) {
                        return 0;
                    }
                    hasmat = false;
                    ++length;
                    if (mat1 != Material.COBBLESTONE) {
                        break;
                    }
                    block.setType(Material.AIR);
                    break;
                }
            }
        }
        if (length > 0 && !hasmat) {
            return length;
        }
        return 0;
    }
    
    public void Track(final Material mat1, final Material mat2, final Player player, final Player player2) {
        final Block block = new Location(player.getWorld(), (double)player.getLocation().getBlockX(), (double)(player.getLocation().getBlockY() - 1), (double)player.getLocation().getBlockZ()).getBlock();
        final boolean northDists = this.findEnd(player.getWorld(), 0, -1, mat1, mat2);
        final boolean southDists = this.findEnd(player.getWorld(), 0, 1, mat1, mat2);
        final boolean eastDists = this.findEnd(player.getWorld(), -1, 0, mat1, mat2);
        final boolean westDists = this.findEnd(player.getWorld(), 1, 0, mat1, mat2);
        final int northDist = this.findBlock(player.getWorld(), 0, -1, mat1, mat2);
        final int southDist = this.findBlock(player.getWorld(), 0, 1, mat1, mat2);
        final int eastDist = this.findBlock(player.getWorld(), -1, 0, mat1, mat2);
        final int westDist = this.findBlock(player.getWorld(), 1, 0, mat1, mat2);
        if (northDists && northDist > 0) {
            this.TrackDir(player, 0, -northDist * 25, player2);
        }
        if (eastDists && eastDist > 0) {
            this.TrackDir(player, -eastDist * 25, 0, player2);
        }
        if (southDists && southDist > 0) {
            this.TrackDir(player, 0, southDist * 25, player2);
        }
        if (westDists && westDist > 0) {
            this.TrackDir(player, westDist * 25, 0, player2);
        }
        if (block.getType() == Material.OBSIDIAN) {
            player.getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, (int)block.getData());
            block.breakNaturally(new ItemStack(Material.AIR));
        }
    }
    
    public void Track(final Player player, final Player player2) {
        final Block block = new Location(player.getWorld(), (double)player.getLocation().getBlockX(), (double)(player.getLocation().getBlockY() - 1), (double)player.getLocation().getBlockZ()).getBlock();
        if (block.getType() == Material.DIAMOND_BLOCK && this.isPerm(player, block)) {
            this.Track(Material.OBSIDIAN, Material.GOLD_BLOCK, player, player2);
        }
        else if (block.getType() == Material.OBSIDIAN && this.isTemp(player, block)) {
            this.Track(Material.COBBLESTONE, Material.STONE, player, player2);
        }
        else {
            player.sendMessage(ChatColor.RED + "You need to be on a tracking block");
        }
    }
    
    public boolean isTemp(final Player p, final Block b1) {
        boolean istemp = false;
        if (b1.getType() == Material.OBSIDIAN) {
            final double left = b1.getLocation().getX() + 1.0;
            final double left2 = b1.getLocation().getX() + 2.0;
            final double right = b1.getLocation().getX() - 1.0;
            final double right2 = b1.getLocation().getX() - 2.0;
            final double front = b1.getLocation().getZ() - 1.0;
            final double front2 = b1.getLocation().getZ() - 2.0;
            final double back = b1.getLocation().getZ() + 1.0;
            final double back2 = b1.getLocation().getZ() + 2.0;
            final Block leftb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), left, b1.getLocation().getY(), b1.getLocation().getZ()));
            final Block leftb2 = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), left2, b1.getLocation().getY(), b1.getLocation().getZ()));
            final Block rightb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), right, b1.getLocation().getY(), b1.getLocation().getZ()));
            final Block rightb2 = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), right2, b1.getLocation().getY(), b1.getLocation().getZ()));
            final Block frontb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), front));
            final Block frontb2 = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), front2));
            final Block backb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), back));
            final Block backb2 = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), back2));
            istemp = (leftb.getType() == Material.STONE || leftb.getType() == Material.COBBLESTONE || (rightb.getType() == Material.STONE || rightb.getType() == Material.COBBLESTONE) || (frontb.getType() == Material.STONE || frontb.getType() == Material.COBBLESTONE) || (backb.getType() == Material.STONE || backb.getType() == Material.COBBLESTONE));
        }
        return istemp;
    }
    
    public boolean isPerm(final Player p, final Block b1) {
        boolean isperm = false;
        if (b1.getType() == Material.DIAMOND_BLOCK) {
            final double left = b1.getLocation().getX() + 1.0;
            final double right = b1.getLocation().getX() - 1.0;
            final double front = b1.getLocation().getZ() - 1.0;
            final double back = b1.getLocation().getZ() + 1.0;
            final Block leftb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), left, b1.getLocation().getY(), b1.getLocation().getZ()));
            final Block rightb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), right, b1.getLocation().getY(), b1.getLocation().getZ()));
            final Block frontb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), front));
            final Block backb = Bukkit.getWorld(p.getWorld().getName()).getBlockAt(new Location(p.getWorld(), b1.getLocation().getX(), b1.getLocation().getY(), back));
            isperm = (leftb.getType() == Material.GOLD_BLOCK || leftb.getType() == Material.OBSIDIAN || (rightb.getType() == Material.GOLD_BLOCK || rightb.getType() == Material.OBSIDIAN) || (frontb.getType() == Material.GOLD_BLOCK || frontb.getType() == Material.OBSIDIAN) || (backb.getType() == Material.GOLD_BLOCK || backb.getType() == Material.OBSIDIAN));
        }
        return isperm;
    }
    
    public void TrackDirAll(final Player player, final int x, final int z, final Player player2) {
        String compass = "West";
        final List<String> in = new ArrayList<String>();
        final int num = Math.abs(x) + Math.abs(z);
        if (player2 == null) {
            for (int i = 0; i < Bukkit.getOnlinePlayers().length; ++i) {
                final Player pl = Bukkit.getOnlinePlayers()[i];
                final boolean can = this.checkPlayer(pl, x, z);
                if (can) {
                    in.add(pl.getName());
                }
            }
        }
        else {
            final boolean can2 = this.checkPlayer(player2, x, z);
            if (can2) {
                in.add(player2.getName());
            }
        }
        if (z < 0) {
            compass = "North";
            if (in.size() == 0) {
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): ");
            }
            else {
                final StringBuilder builder = new StringBuilder();
                for (int j = 0; j < in.size(); ++j) {
                    builder.append(in.get(j)).append(", ");
                }
                String str = builder.toString().trim();
                str = str.substring(0, str.length() - 1);
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): " + ChatColor.GRAY + str);
            }
        }
        else if (x > 0) {
            compass = "East";
            if (in.size() == 0) {
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): ");
            }
            else {
                final StringBuilder builder = new StringBuilder();
                for (int j = 0; j < in.size(); ++j) {
                    builder.append(in.get(j)).append(", ");
                }
                String str = builder.toString().trim();
                str = str.substring(0, str.length() - 1);
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): " + ChatColor.GRAY + str);
            }
        }
        else if (z > 0) {
            compass = "South";
            if (in.size() == 0) {
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): ");
            }
            else {
                final StringBuilder builder = new StringBuilder();
                for (int j = 0; j < in.size(); ++j) {
                    builder.append(in.get(j)).append(", ");
                }
                String str = builder.toString().trim();
                str = str.substring(0, str.length() - 1);
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): " + ChatColor.GRAY + str);
            }
        }
        else if (x < 0) {
            compass = "West";
            if (in.size() == 0) {
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): ");
            }
            else {
                final StringBuilder builder = new StringBuilder();
                for (int j = 0; j < in.size(); ++j) {
                    builder.append(in.get(j)).append(", ");
                }
                String str = builder.toString().trim();
                str = str.substring(0, str.length() - 1);
                player.sendMessage(ChatColor.DARK_AQUA + compass + " (" + num + "): " + ChatColor.GRAY + str);
            }
        }
    }
    
    public void TrackAll(final Material mat1, final Material mat2, final Player player, final Player player2) {
        final Block block = new Location(player.getWorld(), (double)player.getLocation().getBlockX(), (double)(player.getLocation().getBlockY() - 1), (double)player.getLocation().getBlockZ()).getBlock();
        final int northDist = this.findBlock(player.getWorld(), 0, -1, mat1, mat2);
        final int southDist = this.findBlock(player.getWorld(), 0, 1, mat1, mat2);
        final int eastDist = this.findBlock(player.getWorld(), -1, 0, mat1, mat2);
        final int westDist = this.findBlock(player.getWorld(), 1, 0, mat1, mat2);
        final boolean northDists = this.findEnd(player.getWorld(), 0, -1, mat1, mat2);
        final boolean southDists = this.findEnd(player.getWorld(), 0, 1, mat1, mat2);
        final boolean eastDists = this.findEnd(player.getWorld(), -1, 0, mat1, mat2);
        final boolean westDists = this.findEnd(player.getWorld(), 1, 0, mat1, mat2);
        player.sendMessage(ChatColor.DARK_AQUA + "Results:");
        if (northDist > 0 && northDists) {
            this.TrackDirAll(player, 0, -northDist * 25, player2);
        }
        if (eastDist > 0 && eastDists) {
            this.TrackDirAll(player, -eastDist * 25, 0, player2);
        }
        if (southDist > 0 && southDists) {
            this.TrackDirAll(player, 0, southDist * 25, player2);
        }
        if (westDist > 0 && westDists) {
            this.TrackDirAll(player, westDist * 25, 0, player2);
        }
    }
    
    public void TrackAll(final Player player, final Player player2) {
        final Block block = new Location(player.getWorld(), (double)player.getLocation().getBlockX(), (double)(player.getLocation().getBlockY() - 1), (double)player.getLocation().getBlockZ()).getBlock();
        if (block.getType() == Material.DIAMOND_BLOCK && this.isPerm(player, block)) {
            this.TrackAll(Material.OBSIDIAN, Material.GOLD_BLOCK, player, player2);
        }
        else if (this.isTemp(player, block) && block.getType().equals((Object)Material.OBSIDIAN)) {
            player.sendMessage(ChatColor.RED + "You cannot /track all on this type of tracker");
        }
        else {
            player.sendMessage(ChatColor.RED + "You need to be on a tracking block");
        }
    }
    
    public boolean findEnd(final World world, final int x, final int z, final Material mat1, final Material mat2) {
        boolean hasEnd = false;
        for (int i = 1; i < 1000; ++i) {
            final Block block = world.getBlockAt(this.mx + x * i, this.my, this.mz + z * i);
            final Material bmat = block.getType();
            if (bmat == mat2) {
                hasEnd = true;
                break;
            }
        }
        return hasEnd;
    }
}
