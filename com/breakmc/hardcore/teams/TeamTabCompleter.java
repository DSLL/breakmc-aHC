package com.breakmc.hardcore.teams;

import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.breakmc.hardcore.*;
import java.util.*;

public class TeamTabCompleter implements TabCompleter
{
    public List<String> onTabComplete(final CommandSender sender, final Command cmd, final String alias, final String[] args) {
        if (sender instanceof Player && (cmd.getName().equalsIgnoreCase("team") || cmd.getName().equalsIgnoreCase("t"))) {
            if (args.length == 0) {
                Collections.sort(Main.commandargs);
                return Main.commandargs;
            }
            if (args.length == 1) {
                final List<String> list = new ArrayList<String>();
                for (final String s : Main.commandargs) {
                    if (s.toLowerCase().startsWith(args[0].toLowerCase())) {
                        list.add(s);
                    }
                }
                return list;
            }
            if ((args.length == 2 && args[0].equalsIgnoreCase("friendlyfire")) || args[0].equalsIgnoreCase("ff")) {
                return Arrays.asList("on", "off");
            }
        }
        return null;
    }
}
