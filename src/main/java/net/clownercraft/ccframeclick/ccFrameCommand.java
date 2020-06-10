package net.clownercraft.ccframeclick;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ccFrameCommand implements CommandExecutor, TabCompleter {
    public static boolean waiting = false;
    public static UUID waitingUUID;
    public static String waitingCommand;


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {


        if (commandSender.equals(Bukkit.getConsoleSender())) {
            commandSender.sendMessage(Config.must_be_player);
            return true;
        }

        if (!commandSender.hasPermission("ccframeclick.admin")) {
            commandSender.sendMessage(Config.no_permission);
            return true;
        }

        if (args.length<1) {
            commandSender.sendMessage(Config.missing_argument);
            return true;
        }

        UUID uuid = ((Player) commandSender).getUniqueId();

        switch(args[0].toLowerCase()) {
            case "reload":
                //reload the plugin
                Config.init();
                commandSender.sendMessage(Config.reloaded);

                return true;
            case "remove":
                //remove a command from a frame
                String remove = "";

                commandSender.sendMessage(Config.click_to_remove);


                waitingCommand = remove;
                waitingUUID = uuid;
                waiting = true;

                return true;
            case "set":
                //set a command to a frame
                StringBuilder frameCommand = new StringBuilder();

                for (String arg : args) {
                    frameCommand.append(arg).append(" ");
                }
                commandSender.sendMessage(Config.click_to_add);


                waitingCommand = frameCommand.toString();
                waitingUUID = uuid;
                waiting = true;

                return true;
            default:
                commandSender.sendMessage(Config.missing_argument);
                return true;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> out = new ArrayList<>();
        if (strings.length==1) {
            out = Arrays.asList("set", "reload", "remove");
            out = filterList(out,strings[0]);
        }
        return out;
    }

    /**
     * Used for filtering tab-complete results based on what the user started typing
     * @param list the full list to filter
     * @param currStr what the user has typed so far
     * @return the filtered list
     */
    public static ArrayList<String> filterList(List<String> list, String currStr) {
        Pattern filter = Pattern.compile("^"+currStr, Pattern.CASE_INSENSITIVE);
        return list.stream()
                .filter(filter.asPredicate()).collect(Collectors.toCollection(ArrayList::new));
    }
}

