package com.lenis0012.bukkit.npc;

public enum EquipmentSlot
{
    HELMET("HELMET", 0, 4), 
    CHESTPLATE("CHESTPLATE", 1, 3), 
    LEGGINGS("LEGGINGS", 2, 2), 
    BOOTS("BOOTS", 3, 1), 
    HAND("HAND", 4, 0);
    
    private final int id;
    
    private EquipmentSlot(final String s, final int n, final int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
}
