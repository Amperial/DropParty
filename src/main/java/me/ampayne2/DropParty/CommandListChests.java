package me.ampayne2.DropParty;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

public class CommandListChests {

	public void listchests(CommandSender sender) {
		int chestid = 0;
		List<DropPartyChestsTable> list = DatabaseManager.getDatabase().select(DropPartyChestsTable.class).execute().find();
		ListIterator<DropPartyChestsTable> li = list.listIterator();
		while(li.hasNext()){
			String coords = li.next().toString();
			String[] xyz = coords.split(":");
			chestid++;
			sender.sendMessage(ChatColor.AQUA + "Chest " + chestid);
			sender.sendMessage(ChatColor.AQUA + "  x: " + xyz[0]);
			sender.sendMessage(ChatColor.AQUA + "  y: " + xyz[1]);
			sender.sendMessage(ChatColor.AQUA + "  z: " + xyz[2]);
		}
	}

}
