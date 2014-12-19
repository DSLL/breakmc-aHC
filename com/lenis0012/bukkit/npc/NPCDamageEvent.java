package com.lenis0012.bukkit.npc;

import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;

public class NPCDamageEvent extends Event implements Cancellable
{
    private static final HandlerList handlerList;
    private boolean cancelled;
    private final NPC npc;
    private final Entity damager;
    private final EntityDamageEvent.DamageCause cause;
    private double damage;
    
    static {
        handlerList = new HandlerList();
    }
    
    public boolean isCancelled() {
        return this.cancelled;
    }
    
    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }
    
    public NPCDamageEvent(final NPC npc, final Entity damager, final EntityDamageEvent.DamageCause cause, final double damage) {
        super();
        this.cancelled = false;
        this.npc = npc;
        this.damager = damager;
        this.cause = cause;
        this.damage = damage;
    }
    
    public EntityDamageEvent.DamageCause getCause() {
        return this.cause;
    }
    
    public double getDamage() {
        return this.damage;
    }
    
    public void setDamage(final double damage) {
        this.damage = damage;
    }
    
    public NPC getNpc() {
        return this.npc;
    }
    
    public Entity getDamager() {
        return this.damager;
    }
    
    public HandlerList getHandlers() {
        return NPCDamageEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return NPCDamageEvent.handlerList;
    }
}
