package us.wendell.MinecraftManhunt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;

/**
 * @author Wendell Wu, December 2020
 * @version 1.0.1
 * @since 1.0.1
 */
public class HuntersCommand implements CommandExecutor {
    /**
     * The instance of the plugin which we can use to access hunter data
     */
    private final MinecraftManhunt plugin;

    /**
     * Construct a new HuntersCommand which has a reference to the original plugin.
     * @param plugin the MinecraftManhunt plugin instance
     */
    public HuntersCommand(MinecraftManhunt plugin){
        this.plugin = plugin;
    }

    //TODO: this is tightly coupled, I should refactor adding/removing/clearing/printing logic into plugin class
    /**
     * Implements /hunters {add|remove|clear|see} [usernames ...]
     * @param commandSender the sender of the command -- either a Player, CommandBlockSender, or Console
     * @param command what the command being called is.
     * @param label the exact first word of the command
     * @param args the arguments passed to the command
     * @return true if the command processed successfully, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        HashSet<Player> hunters = plugin.getHunters();
        StringBuilder completionMessage = new StringBuilder();
        if(args.length == 0) return false;
        CommandOptions option;
        try{
            option = CommandOptions.valueOf(args[0].toUpperCase());
        }catch (Exception e){
            return false;
        }
        args[0] = null;
        switch(option){
            case ADD:
                List<Player> playersToAdd = HelperMethods.getPlayersFromNames(commandSender, args);
                int numPlayersToAdd = 0;
                for(Player p : playersToAdd){
                    if(!hunters.contains(p) && !plugin.getRunners().contains(p)){
                        p.getInventory().addItem(new ItemStack(Material.COMPASS));
                        hunters.add(p);
                        HelperMethods.notifyPlayer(p, "added", "hunter");
                        numPlayersToAdd++;
                    }else{
                        completionMessage.append(p.getName()).append(ChatColor.LIGHT_PURPLE).append(" is already assigned!\n");
                    }
                }
                if(numPlayersToAdd > 0) completionMessage.append(ChatColor.GREEN).append("Successfully added ").append(numPlayersToAdd).append(" hunter(s)!");
                else completionMessage.append(ChatColor.RED).append("No hunters were added.");
                break;
            case REMOVE:
                List<Player> playersToRemove = HelperMethods.getPlayersFromNames(commandSender, args);
                int numPlayersToRemove = 0;
                for(Player p : playersToRemove){
                    if(hunters.contains(p)){
                        hunters.remove(p);
                        HelperMethods.notifyPlayer(p, "removed", "hunter");
                        numPlayersToRemove++;
                    }else{
                        completionMessage.append(p.getName()).append(ChatColor.LIGHT_PURPLE).append(" is not a hunter!\n");
                    }
                }
                if(numPlayersToRemove > 0) completionMessage.append(ChatColor.GREEN).append("Successfully removed ").append(numPlayersToRemove).append(" hunter(s)!");
                else completionMessage.append(ChatColor.RED).append("No hunters were removed.");
                break;
            case CLEAR:
                for(Player p : hunters)
                    HelperMethods.notifyPlayer(p, "removed", "hunter");
                hunters.clear();
                completionMessage.append("Successfully cleared list of hunters!");
                break;
            case SEE:
                completionMessage.append("The following is the list of hunters:\n");
                for(Player p : hunters) completionMessage.append(p.getName()).append('\n');
                break;
            default:
                return false;
        }
        commandSender.sendMessage(completionMessage.toString());
        return true;
    }
}
