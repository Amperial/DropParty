package me.ampayne2.DropParty;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;

import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyChestsTable;

public class CommandListChests {

	public void listchests() {
		DropPartyChestsTable valuetable = new DropPartyChestsTable();
		Bukkit.getServer().broadcastMessage("called");
		List<DropPartyChestsTable> list = DatabaseManager.getDatabase().select(DropPartyChestsTable.class).execute().find();
		ListIterator<DropPartyChestsTable> li = list.listIterator();
		while(li.hasNext()){
			String value = valuetable.toString();
			Bukkit.getServer().broadcastMessage(value);
		}
	}

}
