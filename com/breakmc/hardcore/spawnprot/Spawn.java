package com.breakmc.hardcore.spawnprot;

import org.bukkit.block.*;
import java.util.*;
import org.bukkit.*;

public class Spawn
{
    Location loc1;
    Location loc2;
    List<Block> blocks;
    
    public Spawn(final Location loc1, final Location loc2) {
        super();
        this.blocks = new ArrayList<Block>();
        this.loc1 = loc1;
        this.loc2 = loc2;
    }
    
    public List<Block> getBlocks() {
        final int minX = (this.loc1.getBlockX() < this.loc2.getBlockX()) ? this.loc1.getBlockX() : this.loc2.getBlockX();
        final int maxX = (this.loc1.getBlockX() > this.loc2.getBlockX()) ? this.loc1.getBlockX() : this.loc2.getBlockX();
        final int maxY = 256;
        final int minZ = (this.loc1.getBlockX() < this.loc2.getBlockX()) ? this.loc1.getBlockX() : this.loc2.getBlockX();
        final int maxZ = (this.loc1.getBlockX() > this.loc2.getBlockX()) ? this.loc1.getBlockX() : this.loc2.getBlockX();
        for (int x = minX; x < maxX; ++x) {
            for (int y = 0; y < maxY; ++y) {
                for (int z = minZ; z < maxZ; ++z) {
                    this.blocks.add(Bukkit.getWorld("world").getBlockAt(x, y, z));
                }
            }
        }
        return this.blocks;
    }
}
