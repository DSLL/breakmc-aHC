package com.breakmc.hardcore.teams;

import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.configuration.*;
import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import org.bukkit.plugin.java.*;
import org.bukkit.*;
import com.breakmc.hardcore.misc.*;
import org.bukkit.plugin.*;
import com.breakmc.hardcore.warps.*;
import java.text.*;

public class TeamUtils
{
    YAMLBuilder teams;
    HashMap<String, String> name;
    
    public TeamUtils() {
        super();
        this.teams = YAMLBuilder.getInstance();
        this.name = new HashMap<String, String>();
    }
    
    public static void createTeam(final String teamName, final String password, final boolean friendlyFire, final List<String> members, final List<String> managers, final Location hq, final Location rally) {
        WarpManager.getInstance();
        final File f = WarpManager.createTeamsFile(teamName);
        final FileConfiguration config = (FileConfiguration)YamlConfiguration.loadConfiguration(f);
        try {
            config.set(String.valueOf(teamName) + ".password", (Object)password);
            config.set(String.valueOf(teamName) + ".friendlyfire", (Object)Boolean.toString(friendlyFire));
            config.set(String.valueOf(teamName) + ".members", (Object)members);
            config.set(String.valueOf(teamName) + ".managers", (Object)managers);
            if (hq != null) {
                config.set(String.valueOf(teamName) + ".hq.world", (Object)hq.getWorld().getName());
                config.set(String.valueOf(teamName) + ".hq.x", (Object)hq.getX());
                config.set(String.valueOf(teamName) + ".hq.y", (Object)hq.getY());
                config.set(String.valueOf(teamName) + ".hq.z", (Object)hq.getZ());
                config.set(String.valueOf(teamName) + ".hq.pitch", (Object)hq.getPitch());
                config.set(String.valueOf(teamName) + ".hq.pitch", (Object)hq.getYaw());
            }
            if (rally != null) {
                config.set(String.valueOf(teamName) + ".rally.world", (Object)rally.getWorld().getName());
                config.set(String.valueOf(teamName) + ".rally.x", (Object)rally.getX());
                config.set(String.valueOf(teamName) + ".rally.y", (Object)rally.getY());
                config.set(String.valueOf(teamName) + ".rally.z", (Object)rally.getZ());
                config.set(String.valueOf(teamName) + ".rally.pitch", (Object)rally.getPitch());
                config.set(String.valueOf(teamName) + ".rally.pitch", (Object)rally.getYaw());
            }
            config.save(f);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public boolean isOnTeam(final OfflinePlayer p) {
        if (this.teams.getTeamData().getConfigurationSection("teams") == null) {
            return false;
        }
        final Set<String> teamlist = (Set<String>)this.teams.getTeamData().getConfigurationSection("teams").getKeys(false);
        for (int i = 0; i < teamlist.size(); ++i) {
            final List<String> teamMembers = (List<String>)this.teams.getTeamData().getStringList("teams." + teamlist.toArray()[i] + ".members");
            final List<String> teamManagers = (List<String>)this.teams.getTeamData().getStringList("teams." + teamlist.toArray()[i] + ".managers");
            if (teamManagers.contains(p.getName())) {
                return true;
            }
            if (teamMembers.contains(p.getName())) {
                return true;
            }
        }
        return false;
    }
    
    public boolean addPlayerToTeam(final OfflinePlayer p, final String name) {
        final ConfigurationSection team = this.teams.getTeamData().getConfigurationSection("teams." + name);
        if (team == null) {
            return false;
        }
        final List<String> teamMembers = (List<String>)team.getStringList("members");
        teamMembers.add(p.getName());
        team.set("members", (Object)teamMembers);
        this.teams.saveTeamData();
        return true;
    }
    
    public boolean removeMembers(final OfflinePlayer p) {
        final HashMap<String, String> oldteam = new HashMap<String, String>();
        if (this.getPlayerTeam(p) == null) {
            return true;
        }
        oldteam.put(p.getName(), this.getPlayerTeam(p));
        this.name.put(p.getName(), this.getPlayerTeam(p));
        final List<String> members = (List<String>)this.teams.getTeamData().getStringList("teams." + this.getPlayerTeam(p) + ".members");
        try {
            members.remove(p.getName());
            this.teams.getTeamData().set("teams." + oldteam.get(p.getName()) + ".members", (Object)members);
            this.teams.saveTeamData();
            oldteam.remove(p.getName());
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public boolean removeManagers(final OfflinePlayer p) {
        final HashMap<String, String> oldteam = new HashMap<String, String>();
        if (this.getPlayerTeam(p) == null) {
            return true;
        }
        oldteam.put(p.getName(), this.getPlayerTeam(p));
        this.name.put(p.getName(), this.getPlayerTeam(p));
        final List<String> managers = (List<String>)this.teams.getTeamData().getStringList("teams." + this.getPlayerTeam(p) + ".managers");
        try {
            managers.remove(p.getName());
            this.teams.getTeamData().set("teams." + oldteam.get(p.getName()) + ".managers", (Object)managers);
            this.teams.saveTeamData();
            oldteam.remove(p.getName());
        }
        catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        return true;
    }
    
    public boolean isManager(final OfflinePlayer p) {
        return this.teams.getTeamData().getStringList("teams." + this.getPlayerTeam(p) + ".managers").contains(p.getName());
    }
    
    public List<String> getMembers(final OfflinePlayer p) {
        return (List<String>)this.teams.getTeamData().getStringList("teams." + this.getPlayerTeam(p) + ".members");
    }
    
    public List<String> getManagers(final OfflinePlayer p) {
        return (List<String>)this.teams.getTeamData().getStringList("teams." + this.getPlayerTeam(p) + ".managers");
    }
    
    public List<String> getMembers(final String name) {
        return (List<String>)this.teams.getTeamData().getStringList("teams." + name + ".members");
    }
    
    public List<String> getManagers(final String name) {
        return (List<String>)this.teams.getTeamData().getStringList("teams." + name + ".managers");
    }
    
    public void addManager(final Player p) {
        final List<String> managers = (List<String>)this.teams.getTeamData().getStringList("teams." + this.name.get(p.getName()) + ".managers");
        managers.add(p.getName());
        this.teams.getTeamData().set("teams." + this.name.get(p.getName()) + ".managers", (Object)managers);
        this.teams.saveTeamData();
    }
    
    public void message(final Player player, final String... message) {
        player.sendMessage(message);
    }
    
    public void messageMembers(final List<String> members, final String... message) {
        for (final String uuid : members) {
            final Player p = Bukkit.getPlayer(uuid);
            if (p == null) {
                continue;
            }
            p.sendMessage(message);
        }
    }
    
    public void messageManagers(final List<String> managers, final String... message) {
        for (final String uuid : managers) {
            final Player p = Bukkit.getPlayer(uuid);
            if (p == null) {
                continue;
            }
            p.sendMessage(message);
        }
    }
    
    public void setPassword(final Player p, final String s) {
        if (!"none".equalsIgnoreCase(s) && !"null".equalsIgnoreCase(s)) {
            this.teams.getTeamData().set("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".password", (Object)s);
        }
        else {
            this.teams.getTeamData().set("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".password", (Object)"");
        }
        this.teams.saveTeamData();
    }
    
    public void warpHQ(final Player p) {
        final World world = Bukkit.getWorld(this.teams.getTeamData().getString("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".hq.world"));
        final double x = this.teams.getTeamData().getDouble("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".hq.x");
        final double y = this.teams.getTeamData().getDouble("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".hq.y");
        final double z = this.teams.getTeamData().getDouble("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".hq.z");
        final float yaw = Float.valueOf(this.teams.getTeamData().getString("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".hq.yaw"));
        final float pitch = Float.valueOf(this.teams.getTeamData().getString("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".hq.pitch"));
        boolean instawarp = true;
        for (final Entity e : p.getNearbyEntities(32.0, 32.0, 32.0)) {
            if (e instanceof Player) {
                final Player pp = (Player)e;
                if (p.getName().equalsIgnoreCase(pp.getName())) {
                    continue;
                }
                if (!this.isOnTeam((OfflinePlayer)p)) {
                    instawarp = false;
                    break;
                }
                if (!this.isOnTeam((OfflinePlayer)pp)) {
                    instawarp = false;
                    break;
                }
                if (this.isOnTeam((OfflinePlayer)pp) && this.isOnTeam((OfflinePlayer)p) && !this.getPlayerTeam((OfflinePlayer)p).equalsIgnoreCase(this.getPlayerTeam((OfflinePlayer)pp))) {
                    instawarp = false;
                    break;
                }
                continue;
            }
        }
        if (!instawarp) {
            p.sendMessage(ChatColor.GRAY + "Someone is nearby! Warping in 10 seconds! Don't move!");
            Main.dontmove.add(p.getName());
            WarpMethods.warpRunnable = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)JavaPlugin.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (Main.dontmove.contains(p.getName())) {
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        CantAttack.add(p);
                        p.sendMessage(ChatColor.GRAY + "You cannot attack for 10 seconds!");
                        Main.dontmove.remove(p.getName());
                    }
                }
            }, 120L);
            return;
        }
        p.teleport(new Location(world, x, y, z, yaw, pitch));
        CantAttack.add(p);
        p.sendMessage(ChatColor.GRAY + "You cannot attack for 10 seconds!");
    }
    
    public void warpRally(final Player p) {
        final World world = Bukkit.getWorld(this.teams.getTeamData().getString("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".rally.world"));
        final double x = this.teams.getTeamData().getDouble("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".rally.x");
        final double y = this.teams.getTeamData().getDouble("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".rally.y");
        final double z = this.teams.getTeamData().getDouble("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".rally.z");
        final float yaw = Float.valueOf(this.teams.getTeamData().getString("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".rally.yaw"));
        final float pitch = Float.valueOf(this.teams.getTeamData().getString("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".rally.pitch"));
        boolean instawarp = true;
        for (final Entity e : p.getNearbyEntities(32.0, 32.0, 32.0)) {
            if (e instanceof Player) {
                final Player pp = (Player)e;
                if (p.getName().equalsIgnoreCase(pp.getName())) {
                    continue;
                }
                if (!this.isOnTeam((OfflinePlayer)p)) {
                    instawarp = false;
                    break;
                }
                if (!this.isOnTeam((OfflinePlayer)pp)) {
                    instawarp = false;
                    break;
                }
                if (this.isOnTeam((OfflinePlayer)pp) && this.isOnTeam((OfflinePlayer)p) && !this.getPlayerTeam((OfflinePlayer)p).equalsIgnoreCase(this.getPlayerTeam((OfflinePlayer)pp))) {
                    instawarp = false;
                    break;
                }
                continue;
            }
        }
        if (!instawarp) {
            p.sendMessage(ChatColor.GRAY + "Someone is nearby! Warping in 10 seconds! Don't move!");
            Main.dontmove.add(p.getName());
            WarpMethods.warpRunnable = Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin)JavaPlugin.getPlugin((Class)Main.class), (Runnable)new Runnable() {
                @Override
                public void run() {
                    if (Main.dontmove.contains(p.getName())) {
                        p.teleport(new Location(world, x, y, z, yaw, pitch));
                        CantAttack.add(p);
                        p.sendMessage(ChatColor.GRAY + "You cannot attack for 10 seconds!");
                        Main.dontmove.remove(p.getName());
                    }
                }
            }, 120L);
            return;
        }
        p.teleport(new Location(world, x, y, z, yaw, pitch));
        CantAttack.add(p);
        p.sendMessage(ChatColor.GRAY + "You cannot attack for 10 seconds!");
    }
    
    public void printOnlineMembers(final Player p, final List<String> members) {
        for (final String uuid : members) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                final OfflinePlayer tmo = Bukkit.getOfflinePlayer(uuid);
                p.sendMessage(ChatColor.GRAY + tmo.getName() + " - Offline");
            }
        }
    }
    
    public void printOfflineMembers(final Player p, final List<String> members) {
        for (final String uuid : members) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                final OfflinePlayer tmo = Bukkit.getOfflinePlayer(uuid);
                p.sendMessage(ChatColor.GRAY + tmo.getName() + " - Offline");
            }
        }
    }
    
    public void printOnlineManagers(final Player p, final List<String> managers) {
        for (final String uuid : managers) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                continue;
            }
            final double health = tm.getHealth() * 5.0;
            final DecimalFormat format = new DecimalFormat("#.##");
            p.sendMessage(ChatColor.DARK_AQUA + tm.getName() + ChatColor.GRAY + " - " + format.format(health) + "% Health");
        }
    }
    
    public void printOfflineManagers(final Player p, final List<String> managers) {
        for (final String uuid : managers) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                final OfflinePlayer tmo = Bukkit.getOfflinePlayer(uuid);
                p.sendMessage(ChatColor.DARK_AQUA + tmo.getName() + ChatColor.GRAY + " - Offline");
            }
        }
    }
    
    public void printOnlineManagers(final Player p, final String name, final List<String> managers) {
        for (final String uuid : managers) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                continue;
            }
            final double health = tm.getHealth() * 5.0;
            final DecimalFormat format = new DecimalFormat("#.##");
            p.sendMessage(ChatColor.DARK_AQUA + tm.getName() + ChatColor.GRAY + " - " + format.format(health) + "% Health");
        }
    }
    
    public void printOfflineManagers(final Player p, final String name, final List<String> managers) {
        for (final String uuid : managers) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                final OfflinePlayer tmo = Bukkit.getOfflinePlayer(uuid);
                p.sendMessage(ChatColor.DARK_AQUA + tmo.getName() + ChatColor.GRAY + " - Offline");
            }
        }
    }
    
    public void printOnlineMembers(final Player p, final String name, final List<String> members) {
        for (final String uuid : members) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                continue;
            }
            final double health = tm.getHealth() * 5.0;
            final DecimalFormat format = new DecimalFormat("#.##");
            p.sendMessage(ChatColor.GRAY + tm.getName() + " - " + format.format(health) + "% Health");
        }
    }
    
    public void printOfflineMembers(final Player p, final String name, final List<String> members) {
        for (final String uuid : members) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm == null) {
                final OfflinePlayer tmo = Bukkit.getOfflinePlayer(uuid);
                p.sendMessage(ChatColor.GRAY + tmo.getName() + " - Offline");
            }
        }
    }
    
    @Deprecated
    public boolean FriendlyFire(final Player p) {
        return this.teams.getTeamData().getBoolean("teams." + this.getPlayerTeam((OfflinePlayer)p) + ".friendlyfire");
    }
    
    public boolean FriendlyFire(final String name, final Player p) {
        boolean friendlyFire = true;
        final List<String> mems = this.getMembers(name);
        final List<String> mans = this.getManagers(name);
        if (mems.contains(p.getName()) || mans.contains(p.getName())) {
            friendlyFire = Boolean.valueOf(this.teams.getTeamData().getString("teams." + name + ".friendlyfire"));
        }
        return friendlyFire;
    }
    
    public String getPlayerTeam(final OfflinePlayer p) {
        String team = null;
        if (this.teams.getTeamData().getConfigurationSection("teams") != null) {
            final Set<String> teamlist = (Set<String>)this.teams.getTeamData().getConfigurationSection("teams").getKeys(false);
            if (teamlist.isEmpty() || teamlist == null) {
                return null;
            }
            for (final String cteams : teamlist) {
                final List<String> teamMembers = (List<String>)this.teams.getTeamData().getStringList("teams." + cteams + ".members");
                final List<String> teamManagers = (List<String>)this.teams.getTeamData().getStringList("teams." + cteams + ".managers");
                if (teamManagers.contains(p.getName())) {
                    team = cteams;
                    break;
                }
                if (teamMembers.contains(p.getName())) {
                    team = cteams;
                    break;
                }
            }
        }
        return team;
    }
    
    public void removeUnusedTeams() {
        if (new File(((Main)JavaPlugin.getPlugin((Class)Main.class)).getDataFolder(), "teams.yml").exists() && this.teams.getTeamData().getConfigurationSection("teams") != null) {
            for (final String name : this.teams.getTeamData().getConfigurationSection("teams").getKeys(false)) {
                final List<String> members = (List<String>)this.teams.getTeamData().getStringList("teams." + name + ".members");
                final List<String> managers = (List<String>)this.teams.getTeamData().getStringList("teams." + name + ".managers");
                if (members.isEmpty() && managers.isEmpty()) {
                    this.teams.getTeamData().set("teams." + name, (Object)null);
                    this.teams.saveTeamData();
                }
            }
        }
    }
    
    public boolean doesTeamExist(final String name) {
        if (new File(((Main)JavaPlugin.getPlugin((Class)Main.class)).getDataFolder(), "teams.yml").exists() && this.teams.getTeamData().getConfigurationSection("teams") != null) {
            for (final String s : this.teams.getTeamData().getConfigurationSection("teams").getKeys(false)) {
                if (s.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public String matchTeamName(final String name) {
        if (new File(((Main)JavaPlugin.getPlugin((Class)Main.class)).getDataFolder(), "teams.yml").exists() && this.teams.getTeamData().getConfigurationSection("teams") != null) {
            for (final String s : this.teams.getTeamData().getConfigurationSection("teams").getKeys(false)) {
                if (s.equalsIgnoreCase(name)) {
                    return s;
                }
            }
        }
        return null;
    }
    
    public Integer countOnlineMembers(final Player p, final List<String> members) {
        int count = 0;
        if (members.isEmpty()) {
            return 0;
        }
        for (final String uuid : members) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm != null) {
                ++count;
            }
        }
        return count;
    }
    
    public Integer countOnlineManagers(final Player p, final List<String> members) {
        int count = members.size();
        if (members.isEmpty()) {
            return 0;
        }
        for (final String uuid : members) {
            final Player tm = Bukkit.getPlayer(uuid);
            if (tm != null) {
                ++count;
            }
        }
        return count;
    }
    
    public void printManagers(final Player p, final List<String> managers) {
        for (final String m : managers) {
            final Player man = Bukkit.getPlayer(m);
            if (man == null) {
                final OfflinePlayer mano = Bukkit.getOfflinePlayer(m);
                p.sendMessage(ChatColor.DARK_AQUA + mano.getName() + ChatColor.GRAY + " - Offline");
            }
            else {
                final double health = man.getHealth() * 5.0;
                final DecimalFormat formHealth = new DecimalFormat("#.##");
                p.sendMessage(ChatColor.DARK_AQUA + man.getName() + ChatColor.GRAY + " - " + formHealth.format(health) + "% Health");
            }
        }
    }
    
    public void printMembers(final Player p, final List<String> members) {
        for (final String m : members) {
            final Player man = Bukkit.getPlayer(m);
            if (man == null) {
                final OfflinePlayer mano = Bukkit.getOfflinePlayer(m);
                p.sendMessage(ChatColor.GRAY + mano.getName() + ChatColor.GRAY + " - Offline");
            }
            else {
                final double health = man.getHealth() * 5.0;
                final DecimalFormat formHealth = new DecimalFormat("#.##");
                p.sendMessage(ChatColor.GRAY + man.getName() + ChatColor.GRAY + " - " + formHealth.format(health) + "% Health");
            }
        }
    }
    
    public void printManagers(final Player p, final String name, final List<String> managers) {
        for (final String m : managers) {
            final Player man = Bukkit.getPlayer(m);
            if (man == null) {
                final OfflinePlayer mano = Bukkit.getOfflinePlayer(m);
                p.sendMessage(ChatColor.DARK_AQUA + mano.getName() + ChatColor.GRAY + " - Offline");
            }
            else {
                final double health = man.getHealth() * 5.0;
                final DecimalFormat formHealth = new DecimalFormat("#.##");
                p.sendMessage(ChatColor.DARK_AQUA + man.getName() + ChatColor.GRAY + " - " + formHealth.format(health) + "% Health");
            }
        }
    }
    
    public void printMembers(final Player p, final String name, final List<String> members) {
        for (final String m : members) {
            final Player man = Bukkit.getPlayer(m);
            if (man == null) {
                final OfflinePlayer mano = Bukkit.getOfflinePlayer(m);
                p.sendMessage(ChatColor.GRAY + mano.getName() + ChatColor.GRAY + " - Offline");
            }
            else {
                final double health = man.getHealth() * 5.0;
                final DecimalFormat formHealth = new DecimalFormat("#.##");
                p.sendMessage(ChatColor.GRAY + man.getName() + ChatColor.GRAY + " - " + formHealth.format(health) + "% Health");
            }
        }
    }
    
    public Location getHQ(final String team) {
        if (this.teams.getTeamData().getConfigurationSection("teams." + team + ".hq") == null) {
            return null;
        }
        final World world = Bukkit.getWorld(this.teams.getTeamData().getString("teams." + team + ".hq.world"));
        final double x = this.teams.getTeamData().getDouble("teams." + team + ".hq.x");
        final double y = this.teams.getTeamData().getDouble("teams." + team + ".hq.y");
        final double z = this.teams.getTeamData().getDouble("teams." + team + ".hq.z");
        final float yaw = Float.valueOf(this.teams.getTeamData().getString("teams." + team + ".hq.yaw"));
        final float pitch = Float.valueOf(this.teams.getTeamData().getString("teams." + team + ".hq.pitch"));
        if (world == null) {
            return null;
        }
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public Location getRally(final String team) {
        if (this.teams.getTeamData().getConfigurationSection("teams." + team + ".rally") == null) {
            return null;
        }
        final World world = Bukkit.getWorld(this.teams.getTeamData().getString("teams." + team + ".rally.world"));
        final double x = this.teams.getTeamData().getDouble("teams." + team + ".rally.x");
        final double y = this.teams.getTeamData().getDouble("teams." + team + ".rally.y");
        final double z = this.teams.getTeamData().getDouble("teams." + team + ".rally.z");
        final float yaw = Float.valueOf(this.teams.getTeamData().getString("teams." + team + ".rally.yaw"));
        final float pitch = Float.valueOf(this.teams.getTeamData().getString("teams." + team + ".rally.pitch"));
        return new Location(world, x, y, z, yaw, pitch);
    }
}
