package com.breakmc.hardcore.runnables;

import org.bukkit.scheduler.*;
import com.breakmc.hardcore.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.entity.*;

public class OverrideRunnable extends BukkitRunnable
{
    public void run() {
        final Iterator<String> iter = Main.overridingWarp.keySet().iterator();
        while (iter.hasNext()) {
            final Player p = Bukkit.getPlayer((String)iter.next());
            if (p == null) {
                continue;
            }
        }
    }
}
