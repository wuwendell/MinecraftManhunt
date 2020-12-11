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
    private MinecraftManhunt plugin;

    /**
     * Construct a new HuntersCommand which has a reference to the original plugin.
     * @param plugin the MinecraftManhunt plugin instance
     */
    public HuntersCommand(MinecraftManhunt plugin){
        this.plugin = plugin;
    }

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
                        numPlayersToAdd++;
                    }else{
                        completionMessage.append(p.getName()).append(ChatColor.LIGHT_PURPLE + " is already assigned!\n");
                    }
                }
                if(numPlayersToAdd > 0){
                    hunters.addAll(playersToAdd);
                    completionMessage.append(ChatColor.GREEN + "Successfully added ").append(numPlayersToAdd).append(" hunter(s)!");
                }else{
                    completionMessage.append(ChatColor.RED + "No hunters were added.");
                }
                break;
            case REMOVE:
                List<Player> playersToRemove = HelperMethods.getPlayersFromNames(commandSender, args);
                int numPlayersToRemove = 0;
                for(Player p : playersToRemove){
                    if(hunters.contains(p)){
                        hunters.remove(p);
                        numPlayersToRemove++;
                    }else{
                        completionMessage.append(p.getName()).append(ChatColor.LIGHT_PURPLE + " is not a hunter!\n");
                    }
                }
                if(numPlayersToRemove > 0){
                    completionMessage.append(ChatColor.GREEN + "Successfully removed ").append(numPlayersToRemove).append(" hunter(s)!");
                }else{
                    completionMessage.append(ChatColor.RED + "No hunters were removed.");
                }
                break;
            case CLEAR:
                hunters.clear();
                completionMessage.append("Successfully cleared list of hunters!");
                break;
            case SEE:
                completionMessage.append("The following is the list of hunters:\n");
                for(Player p : hunters){
                    completionMessage.append(p.getName()).append('\n');
                }
                break;
            default:
                return false;
        }
        commandSender.sendMessage(completionMessage.toString());
        return true;
    }
}
