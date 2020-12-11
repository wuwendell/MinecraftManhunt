package us.wendell.MinecraftManhunt;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;

/**
 * @author Wendell Wu, December 2020
 * @version 1.0.1
 * @since 1.0.1
 */
public class MinecraftManhunt extends JavaPlugin implements Listener {

    //TODO: Handle hunter death -- give new compass upon respawn when the game is started and remove any compasses
    //that they drop
    //TODO: look at Listener tutorial: https://www.youtube.com/watch?v=a9X0EeXtomY&list=PL65-DKRLvp3Yn7iglPfxKoc7bl0N80XgG&index=5
    HashSet<Player> hunters, runners;

    /**
     * Setup everything upon startup, restart, or reload.
     */
    @Override
    public void onEnable(){
        hunters = new HashSet<>();
        runners = new HashSet<>();
    }

    /**
     * Teardown everything upon shutdown, restart, or reload.
     */
    @Override
    public void onDisable(){
        hunters = null;
        runners = null;
    }

    /**
     * Handle a click by any player.
     * @param event the click event
     */
    @EventHandler()
    public void onClick(PlayerInteractEvent event){
        Player hunter = event.getPlayer();
        //if the player is a hunter and they clicked a compass
        if(hunters.contains(hunter) && event.getItem().getType() == Material.COMPASS){
            //try to get the closest player
            Player closestRunner = closestRunnerToLocation(hunter.getLocation());
            if(closestRunner != null){
                hunter.setCompassTarget(closestRunner.getLocation());
                hunter.sendMessage(Color.GREEN + "Now tracking " + closestRunner.getName() + "!");
            }else{
                hunter.sendMessage(Color.RED + "There are no runners to track!");
            }
        }
    }

    /**
     * Given a location, determine the closest runner to the location if there is one
     * in the same world as the given location. Otherwise, return null;
     * @param hunterLocation the given location (presumably of a hunter)
     * @return the closest player in runners who is in the same world as location, otherwise null
     */
    private Player closestRunnerToLocation(Location hunterLocation){
        Player closest = null;
        double closestDistanceSquared = Double.MAX_VALUE;
        for(Player p : runners){
            try{
                // if the player's distance squared is closer than the current closest distance squared
                if(hunterLocation.distanceSquared(p.getLocation()) < closestDistanceSquared){
                    closest = p;
                    closestDistanceSquared = hunterLocation.distanceSquared(p.getLocation());
                }
            }catch(IllegalArgumentException e){ // they were not in the same world
                //don't do anything
            }
        }
        return closest;
    }
}
