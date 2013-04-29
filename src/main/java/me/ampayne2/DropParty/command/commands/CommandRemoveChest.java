package me.ampayne2.DropParty.command.commands;

import java.util.ArrayList;

import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemoveChest implements DropPartyCommand {
	
	private final static ArrayList<String> playersRemoving = new ArrayList<String>();

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
		DatabaseManager.getDatabase().remove(DatabaseManager.getDatabase().select(DropPartyChestsTable.class).where().equal("x",x).and().equal("y",y).and().equal("z",z).execute().findOne());
		player.sendMessage(ChatColor.AQUA + "Chest Removed Successfully.");
		
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String playerName = sender.getName();
		if (CommandSetChest.isSetting(playerName)){
			CommandSetChest.toggleSetting(playerName, sender);
		}
		toggleRemoving(playerName, sender);
	}
}
