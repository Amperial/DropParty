package me.ampayne2.DropParty.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.ampayne2.DropParty.command.commands.CommandListChests;
import me.ampayne2.DropParty.command.commands.CommandListItempoints;
import me.ampayne2.DropParty.command.commands.CommandRemoveChest;
import me.ampayne2.DropParty.command.commands.CommandRemoveItempoint;
import me.ampayne2.DropParty.command.commands.CommandSetChest;
import me.ampayne2.DropParty.command.commands.CommandSetItempoint;
import me.ampayne2.DropParty.command.commands.CommandStart;
import me.ampayne2.DropParty.command.commands.CommandStop;
import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin {

	private final Map<String, DropPartyCommand> commandList = new HashMap<String, DropPartyCommand>();

	public CommandController() {

		commandList.put("start", new CommandStart());
		commandList.put("stop", new CommandStop());
		commandList.put("setchest", new CommandSetChest());
		commandList.put("setitempoint", new CommandSetItempoint());
		commandList.put("removechest", new CommandRemoveChest());
		commandList.put("removeitempoint", new CommandRemoveItempoint());
		commandList.put("listchests", new CommandListChests());
		commandList.put("listitempoints", new CommandListItempoints());
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
			sender.sendMessage(ChatColor.RED
					+ "You do not have permission to use this command.");
			return true;
		}

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED
					+ "Invalid argument. Valid arguments are: "
					+ Arrays.toString(commandList.keySet().toArray()));
			return true;
		}

		if (commandList.containsKey(args[0])) {
			String[] newArgs;
			if (args.length == 0) {
				newArgs = new String[0];
				args = new String[1];
				args[0] = "";
			} else {
				newArgs = new String[args.length - 1];
				System.arraycopy(args, 1, newArgs, 0, args.length - 1);
			}
			commandList.get(args[0]).execute(sender, newArgs);

			return true;
		} else {
			sender.sendMessage(ChatColor.RED
					+ "Invalid argument. Valid arguments are: "
					+ Arrays.toString(commandList.keySet().toArray()));
			return true;
		}
	}

}
