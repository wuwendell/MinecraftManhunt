package us.wendell.MinecraftManhunt;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;

public class HelperMethods {
    /**
     * When processing commands for adding players to a category, do so here.
     * @param sender the entity which issued the command
     * @param names the list of playerNames which we will attempt to return as Player objects.
     *              If any are names are null or only contain whitespace, they will be skipped over.
     *              If a name was not found, the sender is notified.
     * @return the list of player objects associated with the names given, if any were found.
     */
    public static List<Player> getPlayersFromNames(CommandSender sender, String[] names){
        List<Player> retList = new LinkedList<>();
        for(int i = 0; i < names.length; i++){
            if(names[i] == null || names[i].isBlank()) continue;
            Player player = Bukkit.getPlayer(names[i]);
            if(player != null){
                retList.add(player);
            }else{
                sender.sendMessage(ChatColor.RED + "Player " + names[i] + " either does not " +
                        "exist or is not online!");
            }
        }
        return retList;
    }
}
