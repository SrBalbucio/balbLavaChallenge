package balbucio.lavachallenge.game;

import balbucio.lavachallenge.Main;
import balbucio.lavachallenge.Manager;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import net.md_5.bungee.api.chat.TextComponent;

public class EventRunnable implements Runnable {
    @Override
    public void run() {
        if(Manager.getInstance().getArena() != null && Manager.getInstance().getLobby() != null) {
            Manager.getPlayers().clear();
            Manager.getInstance().createGame();
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendMessage(Main.msg("event.message"));
                TextComponent entrar = new TextComponent(Main.msg("event.entrar"));
                entrar.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/lavajoin"));
                player.spigot().sendMessage(entrar);
            }
        }
    }
}
