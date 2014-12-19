package com.lenis0012.bukkit.npc;

public enum NPCAnimation
{
    SWING_ARM("SWING_ARM", 0, 0), 
    DAMAGE("DAMAGE", 1, 1), 
    EAT_FOOD("EAT_FOOD", 2, 3), 
    CRITICAL_HIT("CRITICAL_HIT", 3, 4), 
    MAGIC_CRITICAL_HIT("MAGIC_CRITICAL_HIT", 4, 5), 
    CROUCH("CROUCH", 5, 104), 
    UNCROUCH("UNCROUCH", 6, 105);
    
    private final int id;
    
    private NPCAnimation(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
