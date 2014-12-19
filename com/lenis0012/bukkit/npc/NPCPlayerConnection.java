package com.lenis0012.bukkit.npc;

import net.minecraft.server.v1_7_R3.*;
import org.bukkit.craftbukkit.v1_7_R3.*;
import org.bukkit.*;

public class NPCPlayerConnection extends PlayerConnection
{
    public NPCPlayerConnection(final NetworkManager networkmanager, final EntityPlayer entityplayer) {
        super(((CraftServer)Bukkit.getServer()).getServer(), networkmanager, entityplayer);
    }
}
