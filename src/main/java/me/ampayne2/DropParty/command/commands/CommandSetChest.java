package me.ampayne2.DropParty.command.commands;

import java.util.ArrayList;

import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetChest implements DropPartyCommand {

	private final static ArrayList<String> playersSetting = new ArrayList<String>();

	public static void toggleSetting(String playerName, CommandSender sender) {
		if (isSetting(playerName)) {
			playersSetting.remove(playerName);
			sender.sendMessage(ChatColor.AQUA + "SetChest mode deactivated.");
		} else {
			playersSetting.add(playerName);
			sender.sendMessage(ChatColor.AQUA + "SetChest mode activated.");
		}
	}

	public static boolean isSetting(String playerName) {
		return playersSetting.contains(playerName);
	}

	public static void saveChest(Player player, String playerName, int x, int y, int z) {
		playersSetting.remove(playerName);
		DropPartyChestsTable table = new DropPartyChestsTable();
		table.x = x;
		table.y = y;
		table.z = z;
		DatabaseManager.getDatabase().save(table);
		player.sendMessage(ChatColor.AQUA + "Chest Set Successfully.");
		
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String playerName = sender.getName();
		if (CommandRemoveChest.isRemoving(playerName)){
			CommandRemoveChest.toggleRemoving(playerName, sender);
		}
		toggleSetting(playerName, sender);
	}
}
