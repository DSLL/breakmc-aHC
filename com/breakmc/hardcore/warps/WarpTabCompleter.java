package com.breakmc.hardcore.warps;

import com.breakmc.hardcore.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import java.io.*;
import org.bukkit.configuration.file.*;
import java.util.*;

public class WarpTabCompleter implements TabCompleter
{
    private Main main;
    
    public WarpTabCompleter(final Main main) {
        super();
        this.main = main;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (sender instanceof Player) {
            final Player p = (Player)sender;
            final File f = this.main.getWarpFile(p.getName());
            if (!f.exists()) {
                try {
                    f.createNewFile();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            final FileConfiguration builder = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
            final List<String> args2 = new ArrayList<String>();
            if (args.length == 0) {
                args2.add("list");
                args2.add("set");
                args2.add("del");
                args2.add("delete");
                for (final String s : builder.getConfigurationSection("warps." + p.getName()).getKeys(false)) {
                    args2.add(s);
                    System.out.print(String.valueOf(s) + "\n");
                }
                Collections.sort(args2);
                return args2;
            }
            final int length = args.length;
        }
        return null;
    }
}
