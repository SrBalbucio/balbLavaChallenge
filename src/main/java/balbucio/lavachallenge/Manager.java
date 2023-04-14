package balbucio.lavachallenge;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Manager {

    private File file = new File("plugins/balbLavaChallenge", "arena.yml");
    private YamlConfiguration configuration;

    private Location lobby = null;
    private Location arena = null;

    private static Manager instance;
    private static List<Player> ingame = new ArrayList<>();
    private boolean hasGame = false;
    private boolean isAllowJoin = false;
    private int taskID;

    public Manager() {
        setInstance(this);
        setupFiles();
        setup();
    }

    private void setupFiles(){
        try {
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdir();
                }
                file.createNewFile();
            }
            configuration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setup(){
        if(configuration.contains("lobbylocation")){
            lobby = (Location) configuration.get("lobbylocation");
        }
        if(configuration.contains("arenalocation")){
            arena = (Location) configuration.get("arenalocation");
        }
    }

    public Location getLobby() {
        return lobby;
    }

    public void setLobby(Location lobby) {
        try {
            this.lobby = lobby;
            configuration.set("lobbylocation", lobby);
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Location getArena() {
        return arena;
    }

    public void setArena(Location arena) {
        try {
            this.arena = arena;
            configuration.set("arenalocation", arena);
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Player> getPlayers() {
        return ingame;
    }

    public static Manager getInstance() {
        return instance;
    }

    public static void setInstance(Manager instance) {
        Manager.instance = instance;
    }

    public void addPlayer(Player player){
        ingame.add(player);
    }

    public void removePlayer(Player player, boolean toLobby) {
        ingame.remove(player);
        if (toLobby)
            player.teleport(getLobby());
        if(ingame.size() == 1){
            Player winner = ingame.get(1);
            hasGame = false;
            winner.teleport(getLobby());
            winner.sendMessage(Main.msg("game.win"));
        }
    }

    public boolean hasGame() {
        return hasGame;
    }

    public boolean isAllowJoin() {
        return isAllowJoin;
    }

    public void createGame(){
        ingame.clear();
        isAllowJoin = true;
        hasGame = true;
        taskID = Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if(arena != null && lobby != null) {
                    if (ingame.size() >= Settings.MIN_PLAYERS) {
                        isAllowJoin = false;
                        for (Player player : ingame) {
                            player.sendMessage(Main.msg("event.starting"));
                            player.teleport(getArena());
                            player.setGameMode(GameMode.SURVIVAL);
                            ItemStack itens = new ItemStack(Material.MUSHROOM_SOUP);
                            itens.setAmount(24);
                            player.getInventory().addItem(itens);
                            player.updateInventory();
                        }
                    } else {
                        for (Player player : ingame) {
                            player.sendMessage(Main.msg("event.cancelled"));
                        }
                    }
                }
            }
        }, Settings.COUNTDOWN).getTaskId();
    }

}
