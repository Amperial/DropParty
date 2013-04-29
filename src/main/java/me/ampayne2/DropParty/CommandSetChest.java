package me.ampayne2.DropParty;

import java.util.ArrayList;

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetChest {

	private final static ArrayList<String> playersSetting = new ArrayList<String>();

	public void setChest(String playerName, CommandSender sender) {
		if (CommandRemoveChest.isRemoving(playerName)){
			CommandRemoveChest.toggleRemoving(playerName, sender);
		}
		toggleSetting(playerName, sender);
	}

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

}
