package com.breakmc.hardcore.warps;

import org.bukkit.*;

public class TempLocation
{
    Location loc;
    
    public TempLocation(final Location loc) {
        super();
        this.loc = loc;
    }
    
    public Location getLoc() {
        return this.loc;
    }
    
    public void setLoc(final Location loc) {
        this.loc = loc;
    }
}
