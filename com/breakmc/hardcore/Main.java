package com.breakmc.hardcore;

import org.bukkit.plugin.java.*;
import org.bukkit.permissions.*;
import com.breakmc.hardcore.sql.*;
import java.util.regex.*;
import com.breakmc.hardcore.warps.*;
import com.breakmc.hardcore.spawnprot.*;
import com.breakmc.hardcore.teams.listeners.*;
import com.breakmc.hardcore.misc.*;
import com.breakmc.hardcore.entities.*;
import org.bukkit.plugin.*;
import com.breakmc.hardcore.teams.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.*;
import com.breakmc.hardcore.tracking.*;
import java.io.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;

public class Main extends JavaPlugin implements Listener
{
    public static ArrayList<String> dontmove;
    public static HashMap<String, Boolean> tchat;
    public static HashMap<String, String> overridingWarp;
    public static String teamcommand;
    public static List<String> commandargs;
    public static HashMap<String, String> realUUID;
    public ArrayList<UUID> staffChatLock;
    public Permission MOD_PERMISSION;
    public Map<UUID, Long> reportCooldown;
    public static ChatColor red;
    public static ChatColor green;
    public static ChatColor blue;
    public static ChatColor gray;
    public static ChatColor black;
    public static ChatColor white;
    private static SQLManager sqlManager;
    TeamUtils utils;
    public boolean instawarp;
    public static TreeMap<String, TeamCommand> subCommands;
    YAMLBuilder builder;
    public ArrayList<UUID> hmode;
    static File pfile;
    
    static {
        Main.dontmove = new ArrayList<String>();
        Main.tchat = new HashMap<String, Boolean>();
        Main.overridingWarp = new HashMap<String, String>();
        Main.teamcommand = "";
        Main.commandargs = new ArrayList<String>();
        Main.realUUID = new HashMap<String, String>();
        Main.subCommands = new TreeMap<String, TeamCommand>();
    }
    
    public Main() {
        super();
        this.staffChatLock = new ArrayList<UUID>();
        this.MOD_PERMISSION = new Permission("abundle.mod");
        this.reportCooldown = new HashMap<UUID, Long>();
        this.utils = new TeamUtils();
        this.instawarp = false;
        this.builder = YAMLBuilder.getInstance();
        this.hmode = new ArrayList<UUID>();
    }
    
    public static void main(final String[] args) {
        System.out.println(UUID.randomUUID().toString().length());
        final String name = "Ryan";
        final Pattern pattern = Pattern.compile("([A-Z0-9])", 2);
        final Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            System.out.print(String.valueOf(name) + "\n");
        }
    }
    
    public void onEnable() {
        this.builder.setup((Plugin)this);
        setChatColorVars();
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        new WarpMethods(this);
        new WarpManager(this);
        final File teamFolder = new File(this.getDataFolder() + File.separator + "teams");
        if (!teamFolder.exists()) {
            teamFolder.mkdir();
        }
        setup();
        final PluginManager pm = Bukkit.getPluginManager();
        this.getCommand("t").setTabCompleter((TabCompleter)new TeamTabCompleter());
        this.getCommand("team").setTabCompleter((TabCompleter)new TeamTabCompleter());
        this.getCommand("warp").setTabCompleter((TabCompleter)new WarpTabCompleter(this));
        this.getCommand("go").setTabCompleter((TabCompleter)new WarpTabCompleter(this));
        this.getCommand("warp").setExecutor((CommandExecutor)new Warps());
        this.getCommand("go").setExecutor((CommandExecutor)new Warps());
        this.getCommand("spawn").setExecutor((CommandExecutor)new SpawnProt(this));
        this.getCommand("goas").setExecutor((CommandExecutor)new WarpAdmin());
        this.getCommand("confirm").setExecutor((CommandExecutor)new Yes(this));
        this.getCommand("deny").setExecutor((CommandExecutor)new No());
        this.getCommand("home").setExecutor((CommandExecutor)new Home());
        this.getCommand("sethome").setExecutor((CommandExecutor)new SetHome());
        this.getCommand("getuuid").setExecutor((CommandExecutor)new GetUUID());
        this.getCommand("resetspawn").setExecutor((CommandExecutor)new SpawnReset());
        this.getCommand("homeas").setExecutor((CommandExecutor)new HomeAdmin());
        this.getCommand("teamas").setExecutor((CommandExecutor)new TeamAsCommand());
        this.getCommand("godelas").setExecutor((CommandExecutor)new WarpAdmin());
        pm.registerEvents((Listener)new MoveListener(), (Plugin)this);
        pm.registerEvents((Listener)new ChatListener(), (Plugin)this);
        pm.registerEvents((Listener)new CommandListener(), (Plugin)this);
        pm.registerEvents((Listener)new SpawnProt(this), (Plugin)this);
        pm.registerEvents((Listener)new OnMove(), (Plugin)this);
        pm.registerEvents((Listener)new JoinListener(), (Plugin)this);
        pm.registerEvents((Listener)new XPBottles(), (Plugin)this);
        pm.registerEvents((Listener)new Salvage(), (Plugin)this);
        pm.registerEvents((Listener)new MobCapture(), (Plugin)this);
        pm.registerEvents((Listener)new MobLimit(), (Plugin)this);
        pm.registerEvents((Listener)this, (Plugin)this);
        new XPBottles();
        XPBottles.createRecipes();
        Player[] onlinePlayers;
        for (int length = (onlinePlayers = Bukkit.getOnlinePlayers()).length, i = 0; i < length; ++i) {
            final Player p = onlinePlayers[i];
            SpawnProt.spawnProt.add(p.getName());
            if (this.utils.getPlayerTeam((OfflinePlayer)p) != null) {
                SpawnProt.teamss.put(p.getName(), this.utils.getPlayerTeam((OfflinePlayer)p));
            }
        }
        this.saveDefaultConfig();
        CustomEntityType.registerEntities();
        this.utils.removeUnusedTeams();
    }
    
    public static void setup() {
        Main.subCommands.put("create", new CreateCommand());
        Main.subCommands.put("join", new JoinCommand());
        Main.subCommands.put("leave", new LeaveCommand());
        Main.subCommands.put("chat", new ChatCommand());
        Main.subCommands.put("hq", new HQCommand());
        Main.subCommands.put("rally", new RallyCommand());
        Main.subCommands.put("info", new InfoCommand());
        Main.subCommands.put("roster", new RosterCommand());
        Main.subCommands.put("kick", new KickCommand());
        Main.subCommands.put("promote", new PromoteCommand());
        Main.subCommands.put("demote", new DemoteCommand());
        Main.subCommands.put("pass", new PassCommand());
        Main.subCommands.put("sethq", new SetHQCommand());
        Main.subCommands.put("setrally", new SetRallyCommand());
        Main.subCommands.put("ff", new FriendlyFireCommand());
        for (final TeamCommand team : Main.subCommands.values()) {
            Main.commandargs.add(team.name());
        }
    }
    
    public void onDisable() {
        for (final Entity e : Bukkit.getWorld("world").getEntities()) {
            if (!(e instanceof Player) && !(e instanceof Animals)) {
                e.remove();
            }
        }
        CustomEntityType.unregisterEntities();
        new XPBottles().removeRecipes();
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (command.getName().equalsIgnoreCase("track")) {
            if (((Player)sender).getWorld().getEnvironment() == World.Environment.NETHER || ((Player)sender).getWorld().getEnvironment() == World.Environment.THE_END) {
                final String env = ((Player)sender).getWorld().getEnvironment().equals((Object)World.Environment.NETHER) ? "the Nether." : "The End.";
                sender.sendMessage("§cTracking is disabled in " + env);
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("all")) {
                    final TrackingMethods track = new TrackingMethods();
                    final Player p = (Player)sender;
                    track.setLoc(p.getLocation().getBlockX(), p.getLocation().getBlockY() - 1, p.getLocation().getBlockZ());
                    track.TrackAll((Player)sender, null);
                    return true;
                }
                final Player tracked = Bukkit.getPlayer(args[0]);
                if (tracked == null) {
                    sender.sendMessage("§cCould not find \"" + args[0] + "\"");
                    return true;
                }
                final Player player = (Player)sender;
                final TrackingMethods track2 = new TrackingMethods();
                track2.setLoc(player.getLocation().getBlockX(), player.getLocation().getBlockY() - 1, player.getLocation().getBlockZ());
                track2.Track((Player)sender, tracked);
                return true;
            }
        }
        if (command.getName().equalsIgnoreCase("team") || command.getName().equalsIgnoreCase("t")) {
            Main.teamcommand = label;
            if (args.length == 0) {
                sender.sendMessage("§7==== §3Player Commands §7====");
                for (final TeamCommand teamSubCommands : Main.subCommands.values()) {
                    if (!teamSubCommands.managerOnly()) {
                        sender.sendMessage("§3/" + label + " " + teamSubCommands.usage() + " §7- " + teamSubCommands.info());
                    }
                }
                sender.sendMessage("§7==== §3Manager Commands §7====");
                for (final TeamCommand teamSubTeamCommand : Main.subCommands.values()) {
                    if (teamSubTeamCommand.managerOnly()) {
                        sender.sendMessage("§3/" + label + " " + teamSubTeamCommand.usage() + " §7- " + teamSubTeamCommand.info());
                    }
                }
                return true;
            }
            if (this.getTeamCommand(args[0].toLowerCase()) == null) {
                sender.sendMessage("§cUnrecognized team command!\nDo /" + label + " for more info.");
            }
            else {
                try {
                    final TeamCommand teamcommand = this.getTeamCommand(args[0]);
                    teamcommand.exec((Player)sender, args);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
    
    public TeamCommand getTeamCommand(final String key) {
        TeamCommand tc = null;
        for (final TeamCommand team : Main.subCommands.values()) {
            if (team.name().equalsIgnoreCase(key)) {
                tc = team;
                return tc;
            }
            if (team.aliases() != null && team.aliases().equalsIgnoreCase(key)) {
                tc = team;
                return tc;
            }
        }
        return tc;
    }
    
    public void registerTeamCommand(final String s, final TeamCommand cmd) {
        Main.subCommands.put(s, cmd);
    }
    
    public FileConfiguration getWarpConfig(final String s) {
        final File pfile = new File(this.getDataFolder() + File.separator + "warp", String.valueOf(s) + ".yml");
        if (pfile.exists()) {
            try {
                pfile.createNewFile();
            }
            catch (IOException ex) {}
        }
        return (FileConfiguration)YamlConfiguration.loadConfiguration(pfile);
    }
    
    public File getWarpFile(final String s) {
        return new File(this.getDataFolder() + File.separator + "warp" + File.separator + s + ".yml");
    }
    
    public void saveWarps(final String s) {
        try {
            this.getWarpConfig(s).save(this.getWarpFile(s));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static SQLManager getSQLManager() {
        return Main.sqlManager;
    }
    
    public static void setChatColorVars() {
        Main.red = ChatColor.RED;
        Main.green = ChatColor.GREEN;
        Main.blue = ChatColor.BLUE;
        Main.gray = ChatColor.GRAY;
        Main.black = ChatColor.BLACK;
        Main.white = ChatColor.WHITE;
    }
    
    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        e.setQuitMessage((String)null);
    }
}
