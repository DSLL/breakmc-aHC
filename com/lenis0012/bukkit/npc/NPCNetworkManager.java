package com.lenis0012.bukkit.npc;

import net.minecraft.server.v1_7_R3.*;
import net.minecraft.util.io.netty.channel.*;
import java.net.*;
import java.lang.reflect.*;

public class NPCNetworkManager extends NetworkManager
{
    public NPCNetworkManager() {
        super(false);
        try {
            final Field channel = NetworkManager.class.getDeclaredField("m");
            final Field address = NetworkManager.class.getDeclaredField("n");
            channel.setAccessible(true);
            address.setAccessible(true);
            channel.set(this, new NPCChannel(null));
            address.set(this, new SocketAddress() {
                private static final long serialVersionUID = 6994835504305404545L;
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
