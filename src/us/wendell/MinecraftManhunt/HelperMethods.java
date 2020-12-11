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
        for (String name : names) {
            if (name == null || name.isBlank()) continue;
            Player player = Bukkit.getPlayer(name);
            if (player != null) {
                retList.add(player);
            } else {
                sender.sendMessage(ChatColor.RED + "Player " + name + " either does not " +
                        "exist or is not online!");
            }
        }
        return retList;
    }

    /**
     * Notify a player that they have either been removed/added as a hunter/runner.
     * @param p the player to inform
     * @param removedOrAdded a string, must be "removed" or "added"
     * @param hunterOrRunner a string, must be "hunter" or "runner"
     */
    public static void notifyPlayer(Player p, String removedOrAdded, String hunterOrRunner){
        p.sendMessage((removedOrAdded.equals("removed") ? ChatColor.GOLD : ChatColor.GREEN)
                + "You were " + removedOrAdded + " as a " + hunterOrRunner + "!");
    }
}
