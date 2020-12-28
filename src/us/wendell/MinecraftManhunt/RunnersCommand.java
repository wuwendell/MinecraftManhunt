package us.wendell.MinecraftManhunt;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;

/**
 * @author Wendell Wu, December 2020
 * @version 1.0.1
 * @since 1.0.1
 */
public class RunnersCommand implements CommandExecutor {
    /**
     * The instance of the plugin which we can use to access runner data
     */
    private final MinecraftManhunt plugin;

    /**
     * Construct a new RunnersCommand which has a reference to the original plugin.
     * @param plugin the MinecraftManhunt plugin instance
     */
    public RunnersCommand(MinecraftManhunt plugin){
        this.plugin = plugin;
    }

    //TODO: this is tightly coupled, I should refactor adding/removing/clearing/printing logic into plugin class
    /**
     * Implements /runners {add|remove|clear|see} [usernames ...]
     * @param commandSender the sender of the command -- either a Player, CommandBlockSender, or Console
     * @param command what the command being called is.
     * @param label the exact first word of the command
     * @param args the arguments passed to the command
     * @return true if the command processed successfully, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        Set<Player> runners = plugin.getRunners();
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
                    if(!runners.contains(p) && !plugin.getHunters().contains(p)){
                        runners.add(p);
                        p.setGameMode(GameMode.SURVIVAL);
                        HelperMethods.notifyPlayer(p, "added", "runner");
                        numPlayersToAdd++;
                    }else{
                        completionMessage.append(p.getName()).append(ChatColor.LIGHT_PURPLE).append(" is already assigned!\n");
                    }
                }
                if(numPlayersToAdd > 0) completionMessage.append(ChatColor.GREEN).append("Successfully added ").append(numPlayersToAdd).append(" runner(s)!");
                else completionMessage.append(ChatColor.RED).append("No runners were added.");
                break;
            case REMOVE:
                List<Player> playersToRemove = HelperMethods.getPlayersFromNames(commandSender, args);
                int numPlayersToRemove = 0;
                for(Player p : playersToRemove){
                    if(runners.contains(p)){
                        runners.remove(p);
                        p.setGameMode(GameMode.SPECTATOR);
                        HelperMethods.notifyPlayer(p, "removed", "runner");
                        numPlayersToRemove++;
                    }else{
                        completionMessage.append(p.getName()).append(ChatColor.LIGHT_PURPLE).append(" is not a runner!\n");
                    }
                }
                if(numPlayersToRemove > 0) completionMessage.append(ChatColor.GREEN).append("Successfully removed ").append(numPlayersToRemove).append(" runner(s)!");
                else completionMessage.append(ChatColor.RED).append("No runners were removed.");
                break;
            case CLEAR:
                for(Player p : runners) {
                    HelperMethods.notifyPlayer(p, "removed", "runner");
                    p.setGameMode(GameMode.SPECTATOR);
                }
                runners.clear();
                completionMessage.append("Successfully cleared list of runners!");
                break;
            case SEE:
                completionMessage.append("The following is the list of runners:\n");
                for(Player p : runners) completionMessage.append(p.getName()).append('\n');
                break;
            default:
                return false;
        }
        commandSender.sendMessage(completionMessage.toString());
        return true;
    }
}
