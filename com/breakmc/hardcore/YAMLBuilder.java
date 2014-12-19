package com.breakmc.hardcore;

import org.bukkit.plugin.*;
import java.io.*;
import org.bukkit.configuration.file.*;

public class YAMLBuilder
{
    static YAMLBuilder instance;
    private File wfile;
    private FileConfiguration warps;
    private File tfile;
    private FileConfiguration teams;
    private File cfile;
    private FileConfiguration loggers;
    
    static {
        YAMLBuilder.instance = new YAMLBuilder();
    }
    
    public static YAMLBuilder getInstance() {
        return YAMLBuilder.instance;
    }
    
    public void setup(final Plugin p) {
        this.wfile = new File(p.getDataFolder(), "homes.yml");
        this.tfile = new File(p.getDataFolder(), "teams.yml");
        this.cfile = new File(p.getDataFolder(), "logs.yml");
        if (!this.wfile.exists()) {
            try {
                this.wfile.createNewFile();
            }
            catch (IOException ex) {
                System.out.println("An error has occured while creating warps.yml");
            }
        }
        if (!this.tfile.exists()) {
            try {
                this.tfile.createNewFile();
            }
            catch (IOException ex) {
                System.out.println("An error has occured while creating " + this.tfile.getName());
            }
        }
        this.warps = (FileConfiguration)YamlConfiguration.loadConfiguration(this.wfile);
        this.teams = (FileConfiguration)YamlConfiguration.loadConfiguration(this.tfile);
    }
    
    public FileConfiguration getWarpData() {
        return this.warps;
    }
    
    public void saveWarpData() {
        try {
            this.warps.save(this.wfile);
        }
        catch (IOException ex) {}
    }
    
    public void reloadWarpData() {
        this.warps = (FileConfiguration)YamlConfiguration.loadConfiguration(this.wfile);
    }
    
    public FileConfiguration getTeamData() {
        return this.teams;
    }
    
    public void saveTeamData() {
        try {
            this.teams.save(this.tfile);
        }
        catch (IOException ex) {}
    }
    
    public void reloadTeamData() {
        this.teams = (FileConfiguration)YamlConfiguration.loadConfiguration(this.tfile);
    }
}
