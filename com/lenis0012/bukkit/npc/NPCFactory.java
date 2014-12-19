package com.lenis0012.bukkit.npc;

import org.bukkit.plugin.*;
import org.bukkit.craftbukkit.v1_7_R3.*;
import org.bukkit.metadata.*;
import org.bukkit.*;
import net.minecraft.server.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.*;
import java.util.*;
import org.bukkit.event.server.*;
import org.bukkit.event.*;

public class NPCFactory implements Listener
{
    private final Plugin plugin;
    private final NPCNetworkManager networkManager;
    
    public NPCFactory(final Plugin plugin) {
        super();
        this.plugin = plugin;
        this.networkManager = new NPCNetworkManager();
        Bukkit.getPluginManager().registerEvents((Listener)this, plugin);
    }
    
    public NPC spawnHumanNPC(final Location location, final NPCProfile profile) {
        final World world = location.getWorld();
        final WorldServer worldServer = ((CraftWorld)world).getHandle();
        final NPCEntity entity = new NPCEntity(world, profile, this.networkManager);
        entity.setPositionRotation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        worldServer.addEntity((Entity)entity);
        worldServer.players.remove(entity);
        entity.getBukkitEntity().setMetadata("NPC", (MetadataValue)new FixedMetadataValue(this.plugin, (Object)true));
        return entity;
    }
    
    public NPC getNPC(final org.bukkit.entity.Entity entity) {
        if (!this.isNPC(entity)) {
            return null;
        }
        final NPCEntity npcEntity = (NPCEntity)((CraftEntity)entity).getHandle();
        return npcEntity;
    }
    
    public List<NPC> getNPCs() {
        final List<NPC> npcList = new ArrayList<NPC>();
        for (final World world : Bukkit.getWorlds()) {
            npcList.addAll(this.getNPCs(world));
        }
        return npcList;
    }
    
    public List<NPC> getNPCs(final World world) {
        final List<NPC> npcList = new ArrayList<NPC>();
        for (final org.bukkit.entity.Entity entity : world.getEntities()) {
            if (this.isNPC(entity)) {
                npcList.add(this.getNPC(entity));
            }
        }
        return npcList;
    }
    
    public boolean isNPC(final org.bukkit.entity.Entity entity) {
        return entity.hasMetadata("NPC");
    }
    
    public void despawnAll() {
        for (final World world : Bukkit.getWorlds()) {
            this.despawnAll(world);
        }
    }
    
    public void despawnAll(final World world) {
        for (final org.bukkit.entity.Entity entity : world.getEntities()) {
            if (entity.hasMetadata("NPC")) {
                entity.remove();
            }
        }
    }
    
    @EventHandler
    public void onPluginDisable(final PluginDisableEvent event) {
        if (event.getPlugin().equals(this.plugin)) {
            this.despawnAll();
        }
    }
}
