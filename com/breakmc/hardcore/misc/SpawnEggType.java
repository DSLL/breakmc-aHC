package com.breakmc.hardcore.misc;

import org.bukkit.entity.*;

public enum SpawnEggType
{
    CREEPER("CREEPER", 0, 50, EntityType.CREEPER, 15), 
    SKELETON("SKELETON", 1, 51, EntityType.SKELETON, 25), 
    SPIDER("SPIDER", 2, 52, EntityType.SPIDER, 5), 
    ZOMBIE("ZOMBIE", 3, 54, EntityType.ZOMBIE, 5), 
    SLIME("SLIME", 4, 55, EntityType.SLIME, 5), 
    GHAST("GHAST", 5, 56, EntityType.GHAST, 10), 
    PIG_ZOMBIE("PIG_ZOMBIE", 6, 57, EntityType.PIG_ZOMBIE, 5), 
    ENDERMAN("ENDERMAN", 7, 58, EntityType.ENDERMAN, 10), 
    CAVE_SPIDER("CAVE_SPIDER", 8, 59, EntityType.CAVE_SPIDER, 5), 
    SILVERFISH("SILVERFISH", 9, 60, EntityType.SILVERFISH, 5), 
    BLAZE("BLAZE", 10, 61, EntityType.BLAZE, 5), 
    MAGMA_CUBE("MAGMA_CUBE", 11, 62, EntityType.MAGMA_CUBE, 5), 
    BAT("BAT", 12, 65, EntityType.BAT, 5), 
    WITCH("WITCH", 13, 66, EntityType.WITCH, 15), 
    PIG("PIG", 14, 90, EntityType.PIG, 5), 
    SHEEP("SHEEP", 15, 91, EntityType.SHEEP, 5), 
    COW("COW", 16, 92, EntityType.COW, 5), 
    CHICKEN("CHICKEN", 17, 93, EntityType.CHICKEN, 15), 
    SQUID("SQUID", 18, 94, EntityType.SQUID, 5), 
    WOLF("WOLF", 19, 95, EntityType.WOLF, 5), 
    MUSHROOM_COW("MUSHROOM_COW", 20, 96, EntityType.MUSHROOM_COW, 25), 
    SNOWMAN("SNOWMAN", 21, 97, EntityType.SNOWMAN, 5), 
    OCELOT("OCELOT", 22, 98, EntityType.OCELOT, 5), 
    VILLAGER("VILLAGER", 23, 120, EntityType.VILLAGER, 35), 
    HORSE("HORSE", 24, 100, EntityType.HORSE, 10);
    
    private int id;
    private EntityType entityType;
    private int exp_cost;
    
    private SpawnEggType(final String s, final int n, final int id, final EntityType entityType, final int exp_cost) {
        this.id = id;
        this.entityType = entityType;
        this.exp_cost = exp_cost;
    }
    
    public static SpawnEggType getByName(final String name) {
        if (name == null) {
            return null;
        }
        SpawnEggType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final SpawnEggType spawnEggType = values[i];
            if (spawnEggType.getName().equalsIgnoreCase(name)) {
                return spawnEggType;
            }
        }
        return null;
    }
    
    public static SpawnEggType getByEntityType(final EntityType entityType) {
        if (entityType == null) {
            return null;
        }
        SpawnEggType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final SpawnEggType spawnEggType = values[i];
            if (spawnEggType.getEntityType().equals((Object)entityType)) {
                return spawnEggType;
            }
        }
        return null;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getCost() {
        return this.exp_cost;
    }
    
    public boolean isInstance(final Entity e) {
        return e.getType().equals((Object)this.getEntityType());
    }
    
    public String getName() {
        return this.entityType.getName();
    }
    
    public EntityType getEntityType() {
        return this.entityType;
    }
}
