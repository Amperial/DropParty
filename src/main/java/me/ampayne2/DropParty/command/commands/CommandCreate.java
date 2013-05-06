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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyPartiesTable;

public class CommandCreate implements DropPartyCommand{

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		}else{
			return;
		}
		DropPartyPartiesTable table = new DropPartyPartiesTable();
		table.dpid = dpid;
		if (DatabaseManager.getDatabase().select(DropPartyPartiesTable.class)
				.where().equal("dpid", dpid).execute().findOne() == null) {
			DatabaseManager.getDatabase().save(table);
			sender.sendMessage(ChatColor.AQUA
					+ "Drop Party '" + dpid + "' Created Successfully.");
			return;
		}else{
			sender.sendMessage(ChatColor.RED + "Drop Party '" + dpid + "' Already Exists.");
		}
	}

}
