package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;

public interface TeamCommand
{
    void exec(Player p0, String[] p1);
    
    String name();
    
    String aliases();
    
    String usage();
    
    String info();
    
    boolean managerOnly();
}
