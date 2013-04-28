package me.ampayne2.DropParty;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CommandSetChest {

	private final ArrayList<String> playersSelecting = new ArrayList<String>();

	public void setChest(String playerName, CommandSender sender) {
		toggleSelecting(playerName, sender);
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

	public void saveChest(int x, int y, int z) {

	}

}
