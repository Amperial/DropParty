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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyPartiesTable;
import me.ampayne2.DropParty.database.tables.DropPartySettingsTable;

public class CommandListSettings implements DropPartyCommand{

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		}else{
			return;
		}
		if(DatabaseManager.getDatabase().select(DropPartyPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null){
			sender.sendMessage(ChatColor.RED + "Drop Party '" + dpid + "' Does Not Exist.");
			return;
		}
		DropPartySettingsTable entry = DatabaseManager.getDatabase()
				.select(DropPartySettingsTable.class).where().equal("dpid", dpid).execute().findOne();
		if (entry == null){
			sender.sendMessage(ChatColor.AQUA + "No Drop Party Settings Found.");
			return;
		}else{
			sender.sendMessage(ChatColor.AQUA + "Drop Party " + dpid + " Settings:");
			sender.sendMessage(ChatColor.AQUA + "  Item Delay: " + entry.itemdelay);
			sender.sendMessage(ChatColor.AQUA + "  Max Length: " + entry.maxlength);
			sender.sendMessage(ChatColor.AQUA + "  Max Stack Size: " + entry.maxstack);
		}
	}
}
