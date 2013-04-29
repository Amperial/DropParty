package me.ampayne2.DropParty.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.ampayne2.DropParty.command.commands.CommandListChests;
import me.ampayne2.DropParty.command.commands.CommandRemoveChest;
import me.ampayne2.DropParty.command.commands.CommandRemoveItem;
import me.ampayne2.DropParty.command.commands.CommandSetChest;
import me.ampayne2.DropParty.command.commands.CommandSetItem;
import me.ampayne2.DropParty.command.commands.CommandStart;
import me.ampayne2.DropParty.command.commands.CommandStop;
import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin {

	//This contains all the command this plugin manage.
	//A Map is simply a Key => Value system
	private final Map<String, DropPartyCommand> commandList = new HashMap<String, DropPartyCommand>();

	public CommandController() {

		//We add every command to the commandList map.
		commandList.put("setchest", new CommandSetChest());
		commandList.put("setitem", new CommandSetItem());
		commandList.put("start", new CommandStart());
		commandList.put("stop", new CommandStop());
		commandList.put("listchests", new CommandListChests());
		commandList.put("removechest", new CommandRemoveChest());
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (!cmd.getName().equalsIgnoreCase("dropparty"))
			return false;

		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		}

		if (!sender.hasPermission("dropparty.admin")) {
			sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Invalid argument. Valid arguments are: " + Arrays.toString(commandList.keySet().toArray()));
			return true;
		}

		if (args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Too many arguments.");
			return true;
		}

		if (commandList.containsKey(args[0])) {
			commandList.get(args[0]).execute(sender, args);
			return true;
		} else {
			sender.sendMessage(ChatColor.RED + "Invalid argument. Valid arguments are: " + Arrays.toString(commandList.keySet().toArray()));
			return true;
		}
	}

}
