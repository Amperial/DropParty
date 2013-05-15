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
package me.ampayne2.DropParty.command.commands.list;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.DPPartyController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;

public class CommandListParties implements DPCommand {

	public static String isRunning;

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid = "";
		if (args.length != 0) {
			return;
		}
		List<DPPartiesTable> list = DatabaseManager.getDatabase().select(DPPartiesTable.class).execute().find();
		ListIterator<DPPartiesTable> li = list.listIterator();
		if (list.size() == 0) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpnopartiesfound"), dpid);
			return;
		}
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistparties"), dpid);
		while (li.hasNext()) {
			DPPartiesTable entry = li.next();
			if (DPPartyController.isRunning(entry.dpid)) {
				isRunning = "Running";
			} else {
				isRunning = "Not Running";
			}
			dpid = entry.dpid;
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistparties.party") + isRunning, dpid);
		}
	}
}
