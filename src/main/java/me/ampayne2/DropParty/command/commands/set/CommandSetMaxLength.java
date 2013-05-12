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
package me.ampayne2.DropParty.command.commands.set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPSettingsTable;
import me.ampayne2.DropParty.database.tables.DPTeleportsTable;

public class CommandSetMaxLength implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid;
		int maxlength;
		if (args.length == 2) {
			try {
				maxlength = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "'" + args[0] + "'"
						+ " is not an integer.");
				return;
			}
			dpid = args[1];
		}else{
			return;
		}
		if(DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null){
			sender.sendMessage(ChatColor.RED + "Drop Party '" + dpid + "' Does Not Exist.");
			return;
		}
		DPSettingsTable table = new DPSettingsTable();
		table.dpid = dpid;
		table.maxlength = maxlength;
		if (DatabaseManager.getDatabase().select(DPTeleportsTable.class)
				.where().equal("dpid", dpid).execute().findOne() == null) {
			DatabaseManager.getDatabase().save(table);
			player.sendMessage(ChatColor.AQUA
					+ "Drop Party '" + dpid + "' MaxLength Set Successfully.");
			return;
		}else{
			DPSettingsTable entry = DatabaseManager.getDatabase().select(DPSettingsTable.class).where().equal("dpid", dpid).execute().findOne();
			table.id = entry.id;
		}
		DatabaseManager.getDatabase().save(table);
		player.sendMessage(ChatColor.AQUA
				+ "Drop Party '" + dpid + "' MaxLength Set Successfully.");
	}
}
