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
		int chestid = 0;
		List<DropPartyChestsTable> list = DatabaseManager.getDatabase().select(DropPartyChestsTable.class).execute().find();
		ListIterator<DropPartyChestsTable> li = list.listIterator();
		while(li.hasNext()){
			DropPartyChestsTable entry = li.next();
			sender.sendMessage(ChatColor.AQUA + "Chest " + entry.id + " X:" + entry.x + " Y:" + entry.y + " Z:" + entry.z);
		}
	}
}
