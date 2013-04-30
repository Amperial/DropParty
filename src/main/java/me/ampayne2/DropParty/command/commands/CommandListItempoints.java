package me.ampayne2.DropParty.command.commands;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyItempointsTable;

public class CommandListItempoints implements DropPartyCommand {
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		List<DropPartyItempointsTable> list = DatabaseManager.getDatabase()
				.select(DropPartyItempointsTable.class).execute().find();
		ListIterator<DropPartyItempointsTable> li = list.listIterator();
		if (list.size() == 0) {
			sender.sendMessage(ChatColor.AQUA + "No Drop Party Item Points Found.");
			return;
		}
		int id = 0;
		while (li.hasNext()) {
			DropPartyItempointsTable entry = li.next();
			id++;
			sender.sendMessage(ChatColor.AQUA + "ItemPoint " + id + " X:" + entry.x
					+ " Y:" + entry.y + " Z:" + entry.z);
		}
	}

}
