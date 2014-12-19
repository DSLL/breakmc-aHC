package com.lenis0012.bukkit.npc;

import org.bukkit.event.*;
import org.bukkit.entity.*;

public class NPCInteractEvent extends Event implements Cancellable
{
    private static final HandlerList handlerList;
    private boolean canceled;
    private final NPC npc;
    private final HumanEntity entity;
    
    static {
        handlerList = new HandlerList();
    }
    
    public boolean isCancelled() {
        return this.canceled;
    }
    
    public void setCancelled(final boolean canceled) {
        this.canceled = canceled;
    }
    
    public NPCInteractEvent(final NPC npc, final HumanEntity entity) {
        super();
        this.canceled = false;
        this.npc = npc;
        this.entity = entity;
    }
    
    public NPC getNpc() {
        return this.npc;
    }
    
    public HumanEntity getEntity() {
        return this.entity;
    }
    
    public HandlerList getHandlers() {
        return NPCInteractEvent.handlerList;
    }
    
    public static HandlerList getHandlerList() {
        return NPCInteractEvent.handlerList;
    }
}
