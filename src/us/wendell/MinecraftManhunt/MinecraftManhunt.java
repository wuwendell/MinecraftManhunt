package us.wendell.MinecraftManhunt;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Wendell Wu, December 2020
 * @version 1.0.1
 * @since 1.0.1
 */
public class MinecraftManhunt extends JavaPlugin implements Listener {
    private Set<Player> hunters;
    private Set<Player> runners;

    public Set<Player> getHunters() {
        return hunters;
    }
    public Set<Player> getRunners() {
        return runners;
    }

    /**
     * Setup everything upon startup, restart, or reload.
     */
    @Override
    public void onEnable(){
        //store the hunters and runners in HashSets for quick operations
        hunters = new HashSet<>();
        runners = new HashSet<>();
        //register the two commands we need
        this.getCommand("hunters").setExecutor(new HuntersCommand(this));
        this.getCommand("runners").setExecutor(new RunnersCommand(this));
        //register this class as an EventListening class
        getServer().getPluginManager().registerEvents(this, this);
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
    @EventHandler
    public void onClick(PlayerInteractEvent event){
        Player hunter = event.getPlayer();
        //if the player is a hunter and they clicked a compass
        if(hunters.contains(hunter) && event.getItem() != null && event.getItem().getType() == Material.COMPASS){
            //try to get the closest player
            Player closestRunner = closestRunnerToLocation(hunter.getLocation());
            if(closestRunner != null){ // if we found a player in the same world to track
                if(hunter.getLocation().getWorld().getEnvironment() == World.Environment.NORMAL) {
                    hunter.setCompassTarget(closestRunner.getLocation());
                }else{ // they're in the nether or end, so use lodestone properties
                    CompassMeta compassMetaData = (CompassMeta) event.getItem().getItemMeta();
                    compassMetaData.setLodestone(closestRunner.getLocation());
                    compassMetaData.setLodestoneTracked(false);
                    event.getItem().setItemMeta(compassMetaData);
                }
                hunter.sendMessage(ChatColor.GREEN + "Now tracking " + closestRunner.getName() + "!");
            }else{
                hunter.sendMessage(ChatColor.RED + "There are no runners to track!");
            }
        }
    }

    /**
     * Handle a hunter respawning (we should give them a new compass)
     * @param event a player respawning event
     */
    @EventHandler
    public void onHunterRespawn(PlayerRespawnEvent event){
        Player player = event.getPlayer();
        if(hunters.contains(player)){
            player.getInventory().addItem(new ItemStack(Material.COMPASS));
        }
    }

    /**
     * Set the new player's gamemode to spectator. It should preserve their inventory from any previous session.
     * @param joinEvent the join event.
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent joinEvent){
        joinEvent.getPlayer().setGameMode(GameMode.SPECTATOR);
    }

    /**
     * Remove the player from any list, if they were in them.
     * @param leaveEvent the leave event.
     */
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent leaveEvent){
        Player p = leaveEvent.getPlayer();
        removeHunter(p);
        removeRunner(p);
    }

    /**
     * Add a player to the list of hunters.
     * <p>
     *     Additionally teleports them to the closest hunter in the world, if there is one, and sets their
     *     GameMode to survival. Otherwise, teleports them to their previous spawn.
     * </p>
     * @param p the player to be added, assumed to be online.
     * @return true if the player was successfully added, false if they were already there or failed to add
     */
    public boolean addHunter(Player p){
        //TODO: do this
        if(!hunters.contains(p)){
            hunters.add(p);
            return true;
        }
        return false;
    }

    /**
     * Add a player to the list of runners.
     * <p>
     *     Additionally teleports them to the closest runner in the world, if there is one, and sets their
     *     GameMode to survival. Otherwise, teleports them to their previous spawn.
     * </p>
     * @param p the player to be added, assumed to be online.
     * @return true if the player was successfully added, false if they were already there or failed to add
     */
    public boolean addRunner(Player p){
        //TODO: do this
        if(!runners.contains(p)){
            runners.add(p);
            return true;
        }
        return false;
    }

    /**
     * Remove a player from the hunter's list, if they are there.
     * @param p the player
     * @return true if successfully removed, false if they were not there
     */
    public boolean removeHunter(Player p){
        if(hunters.contains(p)){
            hunters.remove(p);
            return true;
        }
        return false;
    }

    /**
     * Remove a player from the runner's list, if they are there.
     * @param p the player
     * @return true if successfully removed, false if they were not there
     */
    public boolean removeRunner(Player p){
        if(runners.contains(p)){
            runners.remove(p);
            return true;
        }
        return false;
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
