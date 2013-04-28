package me.ampayne2.DropParty;

import java.sql.DriverManager;
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
		getConfig().options().copyDefaults(true);
		saveConfig();
	}

	public void onDisable() {

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (!cmd.getName().equalsIgnoreCase("dropparty"))
			return false;
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED
					+ "This command can only be run by a player.");
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

		String playerName = ((Player) sender).getDisplayName();
		switch (args[0]) {
		case "setchest":
			toggleSelecting(playerName, sender);
			return true;
		case "setitem":
			return true;
		case "start":
			return true;
		case "stop":
			return true;
		default:
			sender.sendMessage(ChatColor.RED
					+ "Invalid argument. Valid arguments are: setchest, setitem, start, and stop.");
			return false;
		}
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
	
	public void saveLocation(String saveType, int chestx, int chesty, int chestz){
		
		String url = getConfig().getString("");
		String user = getConfig().getString("");
		String password = getConfig().getString("");
		switch (saveType){
		case "chest":
			//save to chest table
		case "item":
			//save to item table
		default:
		}
	}
}
