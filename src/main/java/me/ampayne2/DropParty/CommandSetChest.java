package me.ampayne2.DropParty;

import java.util.ArrayList;

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSetChest {

	private final static ArrayList<String> playersSelecting = new ArrayList<String>();

	public void setChest(String playerName, CommandSender sender) {
		toggleSelecting(playerName, sender);
	}

	public static void toggleSelecting(String playerName, CommandSender sender) {
		if (isSelecting(playerName)) {
			playersSelecting.remove(playerName);
			sender.sendMessage(ChatColor.AQUA + "Selection mode deactivated.");
		} else {
			playersSelecting.add(playerName);
			sender.sendMessage(ChatColor.AQUA + "Selection mode activated.");
		}
	}

	public static boolean isSelecting(String playerName) {
		return playersSelecting.contains(playerName);
	}

	public static void saveChest(Player player, String playerName, int x, int y, int z) {
		playersSelecting.remove(playerName);
		DropPartyChestsTable table = new DropPartyChestsTable();
		table.x = x;
		table.y = y;
		table.z = z;
		DatabaseManager.getDatabase().save(table);
		player.sendMessage("chest saved");
		
	}

}
