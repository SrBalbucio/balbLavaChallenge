package balbucio.lavachallenge.commands;

import balbucio.lavachallenge.Main;
import balbucio.lavachallenge.Manager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){ return false; }
        Player player = (Player) sender;
        if(Manager.getInstance().hasGame() && Manager.getInstance().isAllowJoin()){
            Manager.getInstance().addPlayer(player);
            sender.sendMessage(Main.msg("event.hasGame"));
        } else{
            sender.sendMessage(Main.msg("event.notHasGame"));
        }
        return false;
    }
}
