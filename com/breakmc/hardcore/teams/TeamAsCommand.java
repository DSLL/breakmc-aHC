package com.breakmc.hardcore.teams;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class TeamAsCommand implements CommandExecutor
{
    TeamUtils utils;
    
    public TeamAsCommand() {
        super();
        this.utils = new TeamUtils();
    }
    
    public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Console cannot use this command!");
            return true;
        }
        final Player player = (Player)sender;
        if (args.length == 0 || args.length < 2) {
            player.sendMessage(ChatColor.RED + "/" + label + " <player|team> <HQ|Rally>");
            return true;
        }
        boolean team = false;
        final String name = args[0];
        if (this.utils.doesTeamExist(name)) {
            team = true;
        }
        if (team) {
            if (!this.utils.doesTeamExist(this.utils.matchTeamName(name))) {
                player.sendMessage(ChatColor.RED + "There is no player or team with that name!");
                return true;
            }
            final String lowerCase;
            switch (lowerCase = args[1].toLowerCase()) {
                case "hq": {
                    if (this.utils.getHQ(this.utils.matchTeamName(name)) == null) {
                        player.sendMessage(ChatColor.RED + "That team does not have an HQ set!");
                        return true;
                    }
                    player.teleport(this.utils.getHQ(this.utils.matchTeamName(name)));
                    return true;
                }
                case "rally": {
                    if (this.utils.getRally(this.utils.matchTeamName(name)) == null) {
                        player.sendMessage(ChatColor.RED + "That team does not have an HQ set!");
                        return true;
                    }
                    player.teleport(this.utils.getRally(this.utils.matchTeamName(name)));
                    return true;
                }
                default:
                    break;
            }
            player.sendMessage(ChatColor.RED + "/" + label + " <player|team> <HQ|Rally>");
            return true;
        }
        else {
            if (Bukkit.getOfflinePlayer(name) == null || !this.utils.isOnTeam(Bukkit.getOfflinePlayer(name))) {
                player.sendMessage(ChatColor.RED + "There is no player or team with that name!");
                return true;
            }
            final OfflinePlayer of = Bukkit.getOfflinePlayer(name);
            final String teamn = this.utils.getPlayerTeam(of);
            final String lowerCase2;
            switch (lowerCase2 = args[1].toLowerCase()) {
                case "hq": {
                    if (this.utils.getHQ(this.utils.matchTeamName(teamn)) == null) {
                        player.sendMessage(ChatColor.RED + "That team does not have an HQ set!");
                        return true;
                    }
                    player.teleport(this.utils.getHQ(this.utils.matchTeamName(teamn)));
                    return true;
                }
                case "rally": {
                    if (this.utils.getRally(this.utils.matchTeamName(teamn)) == null) {
                        player.sendMessage(ChatColor.RED + "That team does not have a rally point set!");
                        return true;
                    }
                    player.teleport(this.utils.getRally(this.utils.matchTeamName(teamn)));
                    return true;
                }
                default:
                    break;
            }
            player.sendMessage(ChatColor.RED + "/" + label + " <player> <HQ|Rally>");
            return true;
        }
    }
}
