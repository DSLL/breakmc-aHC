package com.lenis0012.bukkit.npc;

import java.net.*;
import net.minecraft.util.io.netty.channel.*;

public class NPCChannel extends AbstractChannel
{
    private final ChannelConfig config;
    
    protected NPCChannel(final Channel parent) {
        super(parent);
        this.config = (ChannelConfig)new DefaultChannelConfig((Channel)this);
    }
    
    public ChannelConfig config() {
        this.config.setAutoRead(true);
        return this.config;
    }
    
    public boolean isActive() {
        return false;
    }
    
    public boolean isOpen() {
        return false;
    }
    
    public ChannelMetadata metadata() {
        return null;
    }
    
    protected void doBeginRead() throws Exception {
    }
    
    protected void doBind(final SocketAddress arg0) throws Exception {
    }
    
    protected void doClose() throws Exception {
    }
    
    protected void doDisconnect() throws Exception {
    }
    
    protected void doWrite(final ChannelOutboundBuffer arg0) throws Exception {
    }
    
    protected boolean isCompatible(final EventLoop arg0) {
        return true;
    }
    
    protected SocketAddress localAddress0() {
        return null;
    }
    
    protected AbstractChannel.AbstractUnsafe newUnsafe() {
        return null;
    }
    
    protected SocketAddress remoteAddress0() {
        return null;
    }
}
