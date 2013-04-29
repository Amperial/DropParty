package me.ampayne2.DropParty;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandController extends JavaPlugin {

	CommandSetChest setChest = new CommandSetChest();
	CommandSetItem setItem = new CommandSetItem();
	CommandStart start = new CommandStart();
	CommandStop stop = new CommandStop();
	CommandListChests listchests = new CommandListChests();
	CommandRemoveChest removechest = new CommandRemoveChest();
	CommandRemoveItem removeitem = new CommandRemoveItem();

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (!cmd.getName().equalsIgnoreCase("dropparty"))
			return false;
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return false;
		}
		if (!sender.hasPermission("dropparty.admin")) {
			sender.sendMessage(ChatColor.RED
					+ "You do not have permission to use this command.");
			return false;
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments.");
			return false;
		}
		if (args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Too many arguments.");
			return false;
		}
		if (!args[0].equalsIgnoreCase("setChest")
				&& !args[0].equalsIgnoreCase("setItem")
				&& !args[0].equalsIgnoreCase("start")
				&& !args[0].equalsIgnoreCase("stop")
				&& !args[0].equalsIgnoreCase("listChests")
				&& !args[0].equalsIgnoreCase("RemoveChest")
				&& !args[0].equalsIgnoreCase("RemoveItem")) {
			sender.sendMessage(ChatColor.RED
					+ "Invalid argument. Valid arguments are: setchest, setitem, removechest, removeitem, listchests, start, and stop.");
			return false;
		}
		String playerName = ((Player) sender).getDisplayName();
		if (args[0].equalsIgnoreCase("setChest")) {
			setChest.setChest(playerName, sender);
		}
		if (args[0].equalsIgnoreCase("setItem")) {
			setItem.setItem(playerName, sender);
		}
		if (args[0].equalsIgnoreCase("start")) {
			start.start();
		}
		if (args[0].equalsIgnoreCase("stop")) {
			stop.stop();
		}
		if (args[0].equalsIgnoreCase("listchests")) {
			listchests.listchests(sender);
		}
		if (args[0].equalsIgnoreCase("RemoveChest")) {
			removechest.removeChest(playerName, sender);
		}
		if (args[0].equalsIgnoreCase("RemoveItem")) {

		}

		return true;
	}

}
