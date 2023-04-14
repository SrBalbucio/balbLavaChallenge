package balbucio.lavachallenge.commands;

import balbucio.lavachallenge.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetupCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cVocê deve ser um player para executar esse comando!");
            return false;
        }
        if(!sender.hasPermission("blava.commands")){
            return false;
        }

        Player player = (Player) sender;
        if(args.length == 0){
            return false;
        }

        String arg = args[0];
        if(arg.equalsIgnoreCase("setlobby")){
            Main.getManager().setLobby(player.getLocation());
            sender.sendMessage(Main.msg("commands.setlobby"));
        } else if(arg.equalsIgnoreCase("setarena")){
            Main.getManager().setArena(player.getLocation());
            sender.sendMessage(Main.msg("commands.setarena"));
        }
        return false;
    }
}
