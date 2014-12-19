package com.breakmc.hardcore.teams;

import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import com.breakmc.hardcore.spawnprot.*;
import org.bukkit.*;
import org.bukkit.configuration.*;
import java.util.*;

public class CreateCommand implements TeamCommand
{
    YAMLBuilder teams;
    TeamUtils utils;
    
    public CreateCommand() {
        super();
        this.teams = YAMLBuilder.getInstance();
        this.utils = new TeamUtils();
    }
    
    @Override
    public void exec(final Player p, final String[] args) {
        if (args.length == 1 || args.length > 3) {
            p.sendMessage(ChatColor.RED + "/" + Main.teamcommand + " " + this.usage());
        }
        if (args.length == 2) {
            if (this.teams.getTeamData().getConfigurationSection("teams") == null || this.teams.getTeamData().getConfigurationSection("teams").getKeys(false).isEmpty()) {
                final ConfigurationSection team = this.teams.getTeamData().createSection("teams." + args[1]);
                team.set("password", (Object)"");
                team.set("friendlyfire", (Object)"false");
                team.set("members", (Object)new ArrayList());
                final List<String> managers = new ArrayList<String>();
                managers.add(p.getName());
                team.set("managers", (Object)managers);
                this.teams.getTeamData().set("inteam." + p.getName(), (Object)args[1]);
                this.teams.saveTeamData();
                SpawnProt.teamss.put(p.getName(), args[1]);
                p.sendMessage(ChatColor.DARK_AQUA + "You have successfully created a team!");
                return;
            }
            if (this.utils.isOnTeam((OfflinePlayer)p)) {
                p.sendMessage(ChatColor.RED + "You are already in a team!");
                return;
            }
            if (this.utils.doesTeamExist(args[1])) {
                p.sendMessage(ChatColor.RED + "That team already exists!");
                return;
            }
            final ConfigurationSection team = this.teams.getTeamData().createSection("teams." + args[1]);
            team.set("password", (Object)"");
            team.set("friendlyfire", (Object)"false");
            team.set("members", (Object)new ArrayList());
            final ArrayList<String> managers2 = new ArrayList<String>();
            managers2.add(p.getName());
            team.set("managers", (Object)managers2);
            this.teams.getTeamData().set("inteam." + p.getName(), (Object)args[1]);
            this.teams.saveTeamData();
            SpawnProt.teamss.put(p.getName(), args[1]);
            p.sendMessage(ChatColor.DARK_AQUA + "You have successfully created a team!");
        }
        else {
            if (args.length != 3) {
                return;
            }
            if (this.teams.getTeamData().getConfigurationSection("teams") == null || this.teams.getTeamData().getConfigurationSection("teams").getKeys(false).isEmpty()) {
                final ConfigurationSection team = this.teams.getTeamData().createSection("teams." + args[1]);
                team.set("password", (Object)args[2]);
                team.set("friendlyfire", (Object)"false");
                team.set("members", (Object)new ArrayList());
                final ArrayList<String> managers2 = new ArrayList<String>();
                managers2.add(p.getName());
                team.set("managers", (Object)managers2);
                this.teams.getTeamData().set("inteam." + p.getName(), (Object)args[1]);
                this.teams.saveTeamData();
                SpawnProt.teamss.put(p.getName(), args[1]);
                p.sendMessage(ChatColor.DARK_AQUA + "You have successfully created a team!");
                return;
            }
            if (this.utils.isOnTeam((OfflinePlayer)p)) {
                p.sendMessage(ChatColor.RED + "You are already in a team!");
                return;
            }
            if (this.utils.doesTeamExist(args[1])) {
                p.sendMessage(ChatColor.RED + "That team already exists!");
                return;
            }
            final ConfigurationSection team = this.teams.getTeamData().createSection("teams." + args[1]);
            team.set("password", (Object)args[2]);
            team.set("friendlyfire", (Object)"false");
            team.set("members", (Object)new ArrayList());
            final ArrayList<String> managers2 = new ArrayList<String>();
            managers2.add(p.getName());
            team.set("managers", (Object)managers2);
            this.teams.getTeamData().set("inteam." + p.getName(), (Object)args[1]);
            this.teams.saveTeamData();
            SpawnProt.teamss.put(p.getName(), args[1]);
            p.sendMessage(ChatColor.DARK_AQUA + "You have successfully created a team!");
        }
    }
    
    @Override
    public String name() {
        return "create";
    }
    
    @Override
    public String aliases() {
        return null;
    }
    
    @Override
    public String info() {
        return "Create a new team.";
    }
    
    @Override
    public String usage() {
        return "create §7(§3Team§7) (§3password§7)";
    }
    
    @Override
    public boolean managerOnly() {
        return false;
    }
}
