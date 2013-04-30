package me.ampayne2.DropParty.command.commands;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

public class CommandListChests implements DropPartyCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		List<DropPartyChestsTable> list = DatabaseManager.getDatabase()
				.select(DropPartyChestsTable.class).execute().find();
		ListIterator<DropPartyChestsTable> li = list.listIterator();
		if (list.size() == 0) {
			sender.sendMessage(ChatColor.AQUA + "No Drop Party Chests Found.");
			return;
		}
		int id = 0;
		while (li.hasNext()) {
			DropPartyChestsTable entry = li.next();
			id++;
			sender.sendMessage(ChatColor.AQUA + "Chest " + id + " X:" + entry.x
					+ " Y:" + entry.y + " Z:" + entry.z);
		}
	}
}
