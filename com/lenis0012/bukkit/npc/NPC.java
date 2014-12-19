package com.lenis0012.bukkit.npc;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;

public interface NPC
{
    Player getBukkitEntity();
    
    boolean isInvulnerable();
    
    void setInvulnerable(boolean p0);
    
    boolean isGravity();
    
    void setGravity(boolean p0);
    
    void setLying(double p0, double p1, double p2);
    
    boolean isLying();
    
    boolean pathfindTo(Location p0);
    
    boolean pathfindTo(Location p0, double p1);
    
    boolean pathfindTo(Location p0, double p1, double p2);
    
    void setTarget(Entity p0);
    
    Entity getTarget();
    
    void lookAt(Location p0);
    
    void setYaw(float p0);
    
    void playAnimation(NPCAnimation p0);
    
    void setEquipment(EquipmentSlot p0, ItemStack p1);
    
    boolean getEntityCollision();
    
    void setEntityCollision(boolean p0);
}
