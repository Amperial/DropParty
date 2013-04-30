/*
 * This file is part of DropParty.
 *
 * Copyright (c) 2013-2013
 *
 * DropParty is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DropParty is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DropParty.  If not, see <http://www.gnu.org/licenses/>.
 */
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
