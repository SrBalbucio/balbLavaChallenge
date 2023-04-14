package balbucio.lavachallenge;

import balbucio.checker.spigot.Checker;
import balbucio.lavachallenge.commands.JoinCommand;
import balbucio.lavachallenge.commands.SetupCommand;
import balbucio.lavachallenge.game.EventRunnable;
import balbucio.lavachallenge.listener.PlayerListener;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.nio.file.Files;

public final class Main extends JavaPlugin {

    private static Main instance;
    private static Manager manager;
    private Checker checker = new Checker("balbLavaChallenge", 1.0, this);
    private File file = new File("plugins/balbLavaChallenge", "config.yml");
    private File msg = new File("plugins/balbLavaChallenge", "messages.yml");
    private File activate = new File("plugins/balbLavaChallenge", "activate.yml");
    private YamlConfiguration configuration, activateConfig;
    private static YamlConfiguration messages;

    @Override
    public void onEnable() {
        checker.checkKey(activateConfig);
        if (checker.hasNewVersion()) {
            if (configuration.getBoolean("autoupdate")) {
                checker.startUpdater();
            } else {
                Bukkit.getConsoleSender().sendMessage("[BalbucioLavaChallenge] §aHá uma nova versão do plugin! Atualize em " + checker.getDetailsPage());
            }
        }
        setupCommands();
        setupListeners();
        manager = new Manager();
        setupSettings();
        startEventScheduler();
    }

    @Override
    public void onLoad() {
        setInstance(this);
        setupFiles();
    }

    @Override
    public void onDisable() {

    }

    private void setupFiles(){
        try {
            if (!file.exists()) {
                File folder = file.getParentFile();
                if (!folder.exists()) {
                    folder.mkdir();
                }
                if(!file.exists()) {
                    Files.copy(this.getClass().getResourceAsStream("/config.yml"), file.toPath());
                }
                if(!activate.exists()) {
                    Files.copy(this.getClass().getResourceAsStream("/activate.yml"), activate.toPath());
                }
                if(!msg.exists()) {
                    Files.copy(this.getClass().getResourceAsStream("/messages.yml"), msg.toPath());
                }
            }
            configuration = YamlConfiguration.loadConfiguration(file);
            activateConfig = YamlConfiguration.loadConfiguration(activate);
            messages = YamlConfiguration.loadConfiguration(msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setupListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
    }

    public static Manager getManager() {
        return manager;
    }

    public void setupSettings(){
        Settings.TEMPO_ENTRE_PARTIDAS = 20*60*configuration.getInt("tempo");
        Settings.COUNTDOWN = 20*60*configuration.getInt("countdown");
        Settings.MIN_PLAYERS = configuration.getInt("min");
    }

    private void setupCommands(){
        this.getCommand("lavasetup").setExecutor(new SetupCommand());
        this.getCommand("lavajoin").setExecutor(new JoinCommand());
    }

    public static String msg(String path){
        return messages.getString(path).replace("&", "§");
    }

    public static Main getInstance() {
        return instance;
    }

    public static void setInstance(Main instance) {
        Main.instance = instance;
    }

    public static void startEventScheduler(){
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new EventRunnable(), Settings.TEMPO_ENTRE_PARTIDAS);
    }
}
