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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPChestsTable;
import me.ampayne2.DropParty.database.tables.DPItemPointsTable;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPSettingsTable;
import me.ampayne2.DropParty.database.tables.DPTeleportsTable;

public class CommandDelete implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		} else {
			return;
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() != null) {
			//remove the party
			DatabaseManager.getDatabase().remove(DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne());
			//remove the settings
			List<DPSettingsTable> settingslist = DatabaseManager.getDatabase().select(DPSettingsTable.class).where().equal("dpid", dpid).execute().find();
			ListIterator<DPSettingsTable> settingsli = settingslist.listIterator();
			while(settingsli.hasNext()){
				DatabaseManager.getDatabase().remove(settingsli.next());
			}
			//remove the teleports
			List<DPTeleportsTable> teleportslist = DatabaseManager.getDatabase().select(DPTeleportsTable.class).where().equal("dpid", dpid).execute().find();
			ListIterator<DPTeleportsTable> teleportsli = teleportslist.listIterator();
			while(teleportsli.hasNext()){
				DatabaseManager.getDatabase().remove(teleportsli.next());
			}
			//remove the chests
			List<DPChestsTable> chestslist = DatabaseManager.getDatabase().select(DPChestsTable.class).where().equal("dpid", dpid).execute().find();
			ListIterator<DPChestsTable> chestsli = chestslist.listIterator();
			while(chestsli.hasNext()){
				DatabaseManager.getDatabase().remove(chestsli.next());
			}
			//remove the itempoints
			List<DPItemPointsTable> itempointslist = DatabaseManager.getDatabase().select(DPItemPointsTable.class).where().equal("dpid", dpid).execute().find();
			ListIterator<DPItemPointsTable> itempointsli = itempointslist.listIterator();
			while(itempointsli.hasNext()){
				DatabaseManager.getDatabase().remove(itempointsli.next());
			}

			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpdelete"), dpid);
			return;
		} else {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
		}
	}
}
