package com.breakmc.hardcore.entities;

import org.bukkit.entity.*;
import java.lang.reflect.*;
import java.util.*;
import net.minecraft.server.v1_7_R3.*;

public enum CustomEntityType
{
    CREEPER("Creeper", 50, EntityType.CREEPER, (Class<? extends EntityInsentient>)EntityCreeper.class, (Class<? extends EntityInsentient>)CustomEntityCreeper.class), 
    SKELETON("Skeleton", 51, EntityType.SKELETON, (Class<? extends EntityInsentient>)EntitySkeleton.class, (Class<? extends EntityInsentient>)CustomEntitySkeleton.class), 
    ZOMBIE("Zombie", 54, EntityType.ZOMBIE, (Class<? extends EntityInsentient>)EntityZombie.class, (Class<? extends EntityInsentient>)CustomEntityZombie.class), 
    SPIDER("Spider", 52, EntityType.SPIDER, (Class<? extends EntityInsentient>)EntitySpider.class, (Class<? extends EntityInsentient>)CustomEntitySpider.class), 
    CAVE_SPIDER("CaveSpider", 59, EntityType.CAVE_SPIDER, (Class<? extends EntityInsentient>)EntityCaveSpider.class, (Class<? extends EntityInsentient>)CustomEntityCaveSpider.class), 
    GHAST("Ghast", 56, EntityType.GHAST, (Class<? extends EntityInsentient>)EntityGhast.class, (Class<? extends EntityInsentient>)CustomEntityGhast.class), 
    SILVERFISH("Silverfish", 60, EntityType.SILVERFISH, (Class<? extends EntityInsentient>)EntitySilverfish.class, (Class<? extends EntityInsentient>)CustomEntitySilverfish.class), 
    BLAZE("Blaze", 61, EntityType.BLAZE, (Class<? extends EntityInsentient>)EntityBlaze.class, (Class<? extends EntityInsentient>)CustomEntityBlaze.class), 
    ZOMBIE_PIGMAN("PigZombie", 57, EntityType.PIG_ZOMBIE, (Class<? extends EntityInsentient>)EntityPigZombie.class, (Class<? extends EntityInsentient>)CustomEntityPigZombie.class), 
    ENDERMAN("Enderman", 58, EntityType.ENDERMAN, (Class<? extends EntityInsentient>)EntityEnderman.class, (Class<? extends EntityInsentient>)CustomEntityEnderman.class), 
    SLIME("Slime", 55, EntityType.SLIME, (Class<? extends EntityInsentient>)EntitySlime.class, (Class<? extends EntityInsentient>)CustomEntitySlime.class), 
    WITCH("Witch", 66, EntityType.WITCH, (Class<? extends EntityInsentient>)EntityWitch.class, (Class<? extends EntityInsentient>)CustomEntityWitch.class), 
    CHICKEN("Chicken", 93, EntityType.CHICKEN, (Class<? extends EntityInsentient>)EntityChicken.class, (Class<? extends EntityInsentient>)CustomChicken.class), 
    COW("Cow", 92, EntityType.COW, (Class<? extends EntityInsentient>)EntityCow.class, (Class<? extends EntityInsentient>)CustomCow.class), 
    PIG("Pig", 90, EntityType.PIG, (Class<? extends EntityInsentient>)EntityPig.class, (Class<? extends EntityInsentient>)CustomPig.class), 
    SHEEP("Sheep", 91, EntityType.SHEEP, (Class<? extends EntityInsentient>)EntitySheep.class, (Class<? extends EntityInsentient>)CustomSheep.class);
    
    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends EntityInsentient> nmsClass;
    private Class<? extends EntityInsentient> customClass;
    
    private CustomEntityType(final String name, final int id, final EntityType entityType, final Class<? extends EntityInsentient> nmsClass, final Class<? extends EntityInsentient> customClass) {
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getID() {
        return this.id;
    }
    
    public EntityType getEntityType() {
        return this.entityType;
    }
    
    public Class<? extends EntityInsentient> getNMSClass() {
        return this.nmsClass;
    }
    
    public Class<? extends EntityInsentient> getCustomClass() {
        return this.customClass;
    }
    
    public static void registerEntities() {
        CustomEntityType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final CustomEntityType entity = values[i];
            a(entity.getCustomClass(), entity.getName(), entity.getID());
        }
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[])getPrivateStatic(BiomeBase.class, "biomes");
        }
        catch (Exception exc) {
            return;
        }
        BiomeBase[] array;
        for (int length2 = (array = biomes).length, j = 0; j < length2; ++j) {
            final BiomeBase biomeBase = array[j];
            if (biomeBase == null) {
                break;
            }
            String[] array2;
            for (int length3 = (array2 = new String[] { "as", "at", "au", "av" }).length, k = 0; k < length3; ++k) {
                final String field = array2[k];
                try {
                    final Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    final List<BiomeMeta> mobList = (List<BiomeMeta>)list.get(biomeBase);
                    for (final BiomeMeta meta : mobList) {
                        CustomEntityType[] values2;
                        for (int length4 = (values2 = values()).length, l = 0; l < length4; ++l) {
                            final CustomEntityType entity2 = values2[l];
                            if (entity2.getNMSClass().equals(meta.b)) {
                                meta.b = entity2.getCustomClass();
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void unregisterEntities() {
        CustomEntityType[] values;
        for (int length = (values = values()).length, i = 0; i < length; ++i) {
            final CustomEntityType entity = values[i];
            try {
                ((Map)getPrivateStatic(EntityTypes.class, "d")).remove(entity.getCustomClass());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ((Map)getPrivateStatic(EntityTypes.class, "f")).remove(entity.getCustomClass());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        CustomEntityType[] values2;
        for (int length2 = (values2 = values()).length, j = 0; j < length2; ++j) {
            final CustomEntityType entity = values2[j];
            try {
                a(entity.getNMSClass(), entity.getName(), entity.getID());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        BiomeBase[] biomes;
        try {
            biomes = (BiomeBase[])getPrivateStatic(BiomeBase.class, "biomes");
        }
        catch (Exception exc) {
            return;
        }
        BiomeBase[] array;
        for (int length3 = (array = biomes).length, k = 0; k < length3; ++k) {
            final BiomeBase biomeBase = array[k];
            if (biomeBase == null) {
                break;
            }
            String[] array2;
            for (int length4 = (array2 = new String[] { "as", "at", "au", "av" }).length, l = 0; l < length4; ++l) {
                final String field = array2[l];
                try {
                    final Field list = BiomeBase.class.getDeclaredField(field);
                    list.setAccessible(true);
                    final List<BiomeMeta> mobList = (List<BiomeMeta>)list.get(biomeBase);
                    for (final BiomeMeta meta : mobList) {
                        CustomEntityType[] values3;
                        for (int length5 = (values3 = values()).length, n = 0; n < length5; ++n) {
                            final CustomEntityType entity2 = values3[n];
                            if (entity2.getCustomClass().equals(meta.b)) {
                                meta.b = entity2.getNMSClass();
                            }
                        }
                    }
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
    
    private static Object getPrivateStatic(final Class clazz, final String f) throws Exception {
        final Field field = clazz.getDeclaredField(f);
        field.setAccessible(true);
        return field.get(null);
    }
    
    private static void a(final Class paramClass, final String paramString, final int paramInt) {
        try {
            ((Map)getPrivateStatic(EntityTypes.class, "c")).put(paramString, paramClass);
            ((Map)getPrivateStatic(EntityTypes.class, "d")).put(paramClass, paramString);
            ((Map)getPrivateStatic(EntityTypes.class, "e")).put(paramInt, paramClass);
            ((Map)getPrivateStatic(EntityTypes.class, "f")).put(paramClass, paramInt);
            ((Map)getPrivateStatic(EntityTypes.class, "g")).put(paramString, paramInt);
        }
        catch (Exception ex) {}
    }
}
