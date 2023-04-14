package balbucio.lavachallenge.listener;

import balbucio.lavachallenge.Manager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent evt){
        Player player = evt.getPlayer();
        player.teleport(Manager.getInstance().getLobby());
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent evt){
        Player player = evt.getPlayer();
        if(Manager.getInstance().hasGame() && Manager.getPlayers().contains(player)){
            Manager.getInstance().removePlayer(player, false);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent evt){
        Player player = evt.getPlayer();
        if(evt.getTo() == Manager.getInstance().getLobby()){
            player.setGameMode(GameMode.ADVENTURE);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent evt){
        Player player = evt.getPlayer();
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent evt){
        Player player = evt.getPlayer();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent evt){
        Player player = evt.getEntity();
        player.spigot().respawn();
        if(Manager.getInstance().hasGame() && Manager.getPlayers().contains(player)){
            Manager.getInstance().removePlayer(player, true);
        }
    }
}
