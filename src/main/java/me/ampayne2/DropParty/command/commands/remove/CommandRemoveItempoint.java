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
package me.ampayne2.DropParty.command.commands.remove;

import java.util.List;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPItemPointsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemoveItempoint implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		String dpid;
		String itempoint;
		if (!sender.hasPermission("dropparty.remove.itempoint") && !sender.hasPermission("dropparty.remove.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		if (args.length == 2) {
			dpid = args[1];
			itempoint = args[0];
		} else {
			return;
		}
		List<DPItemPointsTable> list = DatabaseManager.getDatabase().select(DPItemPointsTable.class).where().equal("dpid", dpid).execute().find();
		Player player = (Player) sender;
		int itempointid;
		try {
			itempointid = Integer.parseInt(itempoint);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "'" + itempoint + "'" + " is not an integer.");
			return;
		}
		if (!(list.size() >= itempointid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpitempointiddoesntexist"), dpid);
			return;
		}
		DPItemPointsTable entry = list.get(itempointid - 1);
		DatabaseManager.getDatabase().remove(entry);
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpremoveitempoint"), dpid);
	}
}
