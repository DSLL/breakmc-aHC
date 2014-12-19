package com.lenis0012.bukkit.npc;

import org.bukkit.craftbukkit.v1_7_R3.*;
import net.minecraft.util.com.mojang.authlib.*;
import org.bukkit.*;
import org.bukkit.util.*;
import org.bukkit.inventory.*;
import org.bukkit.craftbukkit.v1_7_R3.inventory.*;
import java.util.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.*;
import net.minecraft.server.v1_7_R3.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R3.event.*;
import org.bukkit.craftbukkit.v1_7_R3.entity.*;

public class NPCEntity extends EntityPlayer implements NPC
{
    private boolean entityCollision;
    private boolean invulnerable;
    private boolean gravity;
    private boolean lying;
    private Entity target;
    private NPCPath path;
    private final int RADIUS;
    
    public NPCEntity(final World world, final NPCProfile profile, final NPCNetworkManager networkManager) {
        super(((CraftServer)Bukkit.getServer()).getServer(), ((CraftWorld)world).getHandle(), (GameProfile)profile, new PlayerInteractManager((net.minecraft.server.v1_7_R3.World)((CraftWorld)world).getHandle()));
        this.entityCollision = true;
        this.invulnerable = true;
        this.gravity = true;
        this.lying = false;
        this.RADIUS = Bukkit.getViewDistance() * 16;
        this.playerInteractManager.b(EnumGamemode.SURVIVAL);
        this.playerConnection = new NPCPlayerConnection(networkManager, this);
        this.fauxSleeping = true;
        this.bukkitEntity = (CraftEntity)new CraftPlayer((CraftServer)Bukkit.getServer(), (EntityPlayer)this);
    }
    
    public CraftPlayer getBukkitEntity() {
        return (CraftPlayer)this.bukkitEntity;
    }
    
    public boolean isGravity() {
        return this.gravity;
    }
    
    public void setGravity(final boolean gravity) {
        this.gravity = gravity;
    }
    
    public boolean isInvulnerable() {
        return this.invulnerable;
    }
    
    public void setInvulnerable(final boolean invulnerable) {
        this.invulnerable = invulnerable;
    }
    
    public boolean isLying() {
        return this.lying;
    }
    
    public boolean pathfindTo(final Location location) {
        return this.pathfindTo(location, 0.2);
    }
    
    public boolean pathfindTo(final Location location, final double speed) {
        return this.pathfindTo(location, speed, 30.0);
    }
    
    public boolean pathfindTo(final Location location, final double speed, final double range) {
        final NPCPath find;
        final NPCPath path = find = NPCPath.find(this, location, range, speed);
        this.path = find;
        return find != null;
    }
    
    public void setTarget(final Entity target) {
        this.target = target;
        this.lookAt(target.getLocation());
    }
    
    public Entity getTarget() {
        return this.target;
    }
    
    public void lookAt(final Location location) {
        this.setYaw(this.getLocalAngle(new Vector(this.locX, 0.0, this.locZ), location.toVector()));
    }
    
    public void setYaw(final float yaw) {
        this.yaw = yaw;
        this.aP = yaw;
        this.aO = yaw;
    }
    
    private final float getLocalAngle(final Vector point1, final Vector point2) {
        final double dx = point2.getX() - point1.getX();
        final double dz = point2.getZ() - point1.getZ();
        float angle = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0f;
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        return angle;
    }
    
    public void setLying(final double x, final double y, final double z) {
        if (!this.lying) {
            this.broadcastLocalPacket((Packet)new PacketPlayOutBed((EntityHuman)this.getBukkitEntity().getHandle(), (int)x, (int)y, (int)z));
            this.lying = true;
        }
        else if (Double.valueOf(x) == null && Double.valueOf(y) == null && Double.valueOf(z) == null && this.lying) {
            this.broadcastLocalPacket((Packet)new PacketPlayOutAnimation((net.minecraft.server.v1_7_R3.Entity)this, 2));
            this.lying = false;
        }
    }
    
    public void playAnimation(final NPCAnimation animation) {
        this.broadcastLocalPacket((Packet)new PacketPlayOutAnimation((net.minecraft.server.v1_7_R3.Entity)this, animation.getId()));
    }
    
    public void setEquipment(final EquipmentSlot slot, final ItemStack item) {
        this.broadcastLocalPacket((Packet)new PacketPlayOutEntityEquipment(this.getId(), slot.getId(), CraftItemStack.asNMSCopy(item)));
    }
    
    private final void broadcastLocalPacket(final Packet packet) {
        for (final Player p : this.getBukkitEntity().getWorld().getPlayers()) {
            if (this.getBukkitEntity().getLocation().distanceSquared(p.getLocation()) <= this.RADIUS * this.RADIUS) {
                ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
    
    public void h() {
        super.h();
        this.C();
        if (this.target != null && this.path == null) {
            if (this.target.isDead() || (this.target instanceof Player && !((Player)this.target).isOnline())) {
                this.target = null;
            }
            else if (this.getBukkitEntity().getLocation().getWorld().equals(this.target.getWorld()) && this.getBukkitEntity().getLocation().distanceSquared(this.target.getLocation()) <= 1024.0) {
                this.lookAt(this.target.getLocation());
            }
        }
        if (this.path != null && !this.path.update()) {
            this.path = null;
        }
        if (this.world.getType(MathHelper.floor(this.locX), MathHelper.floor(this.locY), MathHelper.floor(this.locZ)).getMaterial() == Material.FIRE) {
            this.setOnFire(15);
        }
        this.motY = (this.onGround ? Math.max(0.0, this.motY) : this.motY);
        this.move(this.motX, this.motY, this.motZ);
        this.motX *= 0.800000011920929;
        this.motY *= 0.800000011920929;
        this.motZ *= 0.800000011920929;
        if (this.gravity && !this.onGround) {
            this.motY -= 0.1;
        }
    }
    
    public boolean a(final EntityHuman entity) {
        final NPCInteractEvent event = new NPCInteractEvent(this, (HumanEntity)this.getBukkitEntity());
        Bukkit.getPluginManager().callEvent((Event)event);
        return !event.isCancelled() && super.a(entity);
    }
    
    public boolean damageEntity(final DamageSource source, final float damage) {
        if (this.invulnerable || this.noDamageTicks > 0) {
            return false;
        }
        EntityDamageEvent.DamageCause cause = null;
        Entity bEntity = null;
        if (source instanceof EntityDamageSource) {
            net.minecraft.server.v1_7_R3.Entity damager = source.getEntity();
            cause = EntityDamageEvent.DamageCause.ENTITY_ATTACK;
            if (source instanceof EntityDamageSourceIndirect) {
                damager = ((EntityDamageSourceIndirect)source).getProximateDamageSource();
                if (damager.getBukkitEntity() instanceof ThrownPotion) {
                    cause = EntityDamageEvent.DamageCause.MAGIC;
                }
                else if (damager.getBukkitEntity() instanceof Projectile) {
                    cause = EntityDamageEvent.DamageCause.PROJECTILE;
                }
            }
            bEntity = (Entity)damager.getBukkitEntity();
        }
        else if (source == DamageSource.FIRE) {
            cause = EntityDamageEvent.DamageCause.FIRE;
        }
        else if (source == DamageSource.STARVE) {
            cause = EntityDamageEvent.DamageCause.STARVATION;
        }
        else if (source == DamageSource.WITHER) {
            cause = EntityDamageEvent.DamageCause.WITHER;
        }
        else if (source == DamageSource.STUCK) {
            cause = EntityDamageEvent.DamageCause.SUFFOCATION;
        }
        else if (source == DamageSource.DROWN) {
            cause = EntityDamageEvent.DamageCause.DROWNING;
        }
        else if (source == DamageSource.BURN) {
            cause = EntityDamageEvent.DamageCause.FIRE_TICK;
        }
        else if (source == CraftEventFactory.MELTING) {
            cause = EntityDamageEvent.DamageCause.MELTING;
        }
        else if (source == CraftEventFactory.POISON) {
            cause = EntityDamageEvent.DamageCause.POISON;
        }
        else if (source == DamageSource.MAGIC) {
            cause = EntityDamageEvent.DamageCause.MAGIC;
        }
        else if (source == DamageSource.OUT_OF_WORLD) {
            cause = EntityDamageEvent.DamageCause.VOID;
        }
        if (cause != null) {
            final NPCDamageEvent event = new NPCDamageEvent(this, bEntity, cause, damage);
            Bukkit.getPluginManager().callEvent((Event)event);
            return !event.isCancelled() && super.damageEntity(source, (float)event.getDamage());
        }
        if (super.damageEntity(source, damage)) {
            if (bEntity != null) {
                final net.minecraft.server.v1_7_R3.Entity e = ((CraftEntity)bEntity).getHandle();
                double d0;
                double d;
                for (d0 = e.locX - this.locX, d = e.locZ - this.locZ; d0 * d0 + d * d < 1.0E-4; d0 = (Math.random() - Math.random()) * 0.01, d = (Math.random() - Math.random()) * 0.01) {}
                this.a(e, damage, d0, d);
            }
            return true;
        }
        return false;
    }
    
    public boolean getEntityCollision() {
        return this.entityCollision;
    }
    
    public void setEntityCollision(final boolean entityCollision) {
        this.entityCollision = entityCollision;
    }
    
    public void g(final double x, final double y, final double z) {
        if (this.getBukkitEntity() != null && this.getEntityCollision()) {
            super.g(x, y, z);
        }
    }
}
