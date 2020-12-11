package us.wendell.MinecraftManhunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

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
     * Implements /hunters {add|remove|clear} [usernames ...]
     * @param commandSender the sender of the command -- either a Player, CommandBlockSender, or Console
     * @param command what the command being called is.
     * @param label the exact first word of the command
     * @param args the arguments passed to the command
     * @return true if the command processed successfully, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        switch(CommandOptions.valueOf(args[0])){
            case Add:
                return true;

            case Remove:
                return true;

            case Clear:
                return true;

            default:
                return false;
        }
    }
}
