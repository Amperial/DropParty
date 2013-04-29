package me.ampayne2.DropParty;

import java.util.ArrayList;

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemoveChest {
	
	private final static ArrayList<String> playersRemoving = new ArrayList<String>();

	public void removeChest(String playerName, CommandSender sender) {
		if (CommandSetChest.isSetting(playerName)){
			CommandSetChest.toggleSetting(playerName, sender);
		}
		toggleRemoving(playerName, sender);
		
	}

	public static void toggleRemoving(String playerName, CommandSender sender) {
		if (isRemoving(playerName)) {
			playersRemoving.remove(playerName);
			sender.sendMessage(ChatColor.AQUA + "RemoveChest mode deactivated.");
		} else {
			playersRemoving.add(playerName);
			sender.sendMessage(ChatColor.AQUA + "RemoveChest mode activated.");
		}
	}

	public static boolean isRemoving(String playerName) {
		return playersRemoving.contains(playerName);
	}

	public static void deleteChest(Player player, String playerName, int x, int y, int z) {
		playersRemoving.remove(playerName);
		DropPartyChestsTable table = new DropPartyChestsTable();
		table.x = x;
		table.y = y;
		table.z = z;
		DatabaseManager.getDatabase().select(DropPartyChestsTable.class).where().equal("x",x).and().equal("y",y).and().equal("z",z).execute().findOne();
		DatabaseManager.getDatabase().remove(table);
		player.sendMessage(ChatColor.AQUA + "Chest Removed Successfully.");
		
	}

}
