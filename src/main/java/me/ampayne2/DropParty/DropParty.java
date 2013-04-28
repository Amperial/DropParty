package me.ampayne2.DropParty;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DropParty extends JavaPlugin {

	private final ArrayList<String> playersSelecting = new ArrayList<String>();

	public void onEnable() {
		PluginManager manager = this.getServer().getPluginManager();
		manager.registerEvents(new DropPartyListener(), this);
		DropPartyListener.plugin = this;
	}

	public void onDisable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (!cmd.getName().equalsIgnoreCase("dropparty"))
			return false;

		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
			return false;
		}

		if (!sender.hasPermission("dropparty.admin"))
			return false;

		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Not enough arguments.");
			return false;
		}

		if (args.length > 1) {
			sender.sendMessage(ChatColor.RED + "Too many arguments.");
			return false;
		}

		if (!args[0].equalsIgnoreCase("setchest")
				&& !args[0].equalsIgnoreCase("setitem")
				&& !args[0].equalsIgnoreCase("start")
				&& !args[0].equalsIgnoreCase("stop")) {
			sender.sendMessage(ChatColor.RED
					+ "Invalid argument. Valid arguments are: setchest, setitem, start, and stop.");
			return false;
		}

		String playerName = ((Player) sender).getDisplayName();

		if (args[0].equals("setchest")) {
			toggleSelecting(playerName, sender);
			return true;
		}
		if (args[0].equals("setitem")) {
			return true;
		}
		if (args[0].equals("start")) {
			return true;
		}
		if (args[0].equals("stop")) {
			return true;
		}
		return false;
	}

	public void toggleSelecting(String playerName, CommandSender sender) {
		if (isSelecting(playerName)) {
			playersSelecting.remove(playerName);
			sender.sendMessage(ChatColor.AQUA + "Selection mode deactivated.");
		} else {
			playersSelecting.add(playerName);
			sender.sendMessage(ChatColor.AQUA + "Selection mode activated.");
		}
	}

	public boolean isSelecting(String playerName) {
		return playersSelecting.contains(playerName);
	}
}
