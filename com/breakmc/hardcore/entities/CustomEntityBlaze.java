package com.breakmc.hardcore.entities;

import net.minecraft.server.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.util.*;
import java.lang.reflect.*;

public class CustomEntityBlaze extends EntityBlaze
{
    public CustomEntityBlaze(final World world) {
        super(world);
        try {
            final Field bField = PathfinderGoalSelector.class.getDeclaredField("b");
            bField.setAccessible(true);
            final Field cField = PathfinderGoalSelector.class.getDeclaredField("c");
            cField.setAccessible(true);
            bField.set(this.goalSelector, new UnsafeList());
            bField.set(this.targetSelector, new UnsafeList());
            cField.set(this.goalSelector, new UnsafeList());
            cField.set(this.targetSelector, new UnsafeList());
        }
        catch (Exception exc) {
            exc.printStackTrace();
        }
    }
}
