package com.breakmc.hardcore.warps;

import com.breakmc.hardcore.*;
import java.io.*;
import org.bukkit.configuration.file.*;

public class WarpManager
{
    private static Main plugin;
    private static WarpManager instance;
    
    static {
        WarpManager.instance = new WarpManager(WarpManager.plugin);
    }
    
    public WarpManager(final Main plugin) {
        super();
        WarpManager.plugin = plugin;
    }
    
    public static WarpManager getInstance() {
        return WarpManager.instance;
    }
    
    public static File createTeamsFile(final String name) {
        if (!new File(WarpManager.plugin.getDataFolder() + File.separator + "teams").exists()) {
            new File(WarpManager.plugin.getDataFolder() + File.separator + "teams").mkdir();
        }
        final File tfile = new File(WarpManager.plugin.getDataFolder() + File.separator + "teams" + File.separator + name + ".yml");
        if (!tfile.exists()) {
            try {
                tfile.createNewFile();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return tfile;
    }
    
    public static FileConfiguration getTeamsFile(final String name) {
        final File file = new File(WarpManager.plugin.getDataFolder() + File.separator + "teams" + File.separator + name + ".yml");
        if (file.exists()) {
            return (FileConfiguration)YamlConfiguration.loadConfiguration(file);
        }
        return null;
    }
    
    public static void createFile(final String s) {
        final File pfile = new File(WarpManager.plugin.getDataFolder() + File.separator + "warp" + File.separator + s + ".yml");
        if (!pfile.exists()) {
            try {
                pfile.createNewFile();
            }
            catch (IOException ex) {}
        }
    }
}
