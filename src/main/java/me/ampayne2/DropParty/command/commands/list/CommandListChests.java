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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPChestsTable;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;

public class CommandListChests implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		Player player = (Player) sender;
		if (args.length == 1) {
			dpid = args[0];
		} else {
			return;
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		List<DPChestsTable> list = DatabaseManager.getDatabase().select(DPChestsTable.class).where().equal("dpid", dpid).execute().find();
		ListIterator<DPChestsTable> li = list.listIterator();
		if (list.size() == 0) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpnochestsfound"), dpid);
			return;
		} else {
			int id = 0;
			sender.sendMessage(ChatColor.AQUA + "Drop Party '" + dpid + "' Chests:");
			while (li.hasNext()) {
				DPChestsTable entry = li.next();
				id++;
				sender.sendMessage(ChatColor.AQUA + "Chest " + id + " World: " + entry.world + " X: " + entry.x + " Y: " + entry.y + " Z: " + entry.z);
			}
		}
	}
}
