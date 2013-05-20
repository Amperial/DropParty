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

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPTeleportsTable;

public class CommandListTeleport implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		} else {
			return;
		}
		if (!sender.hasPermission("dropparty.list.teleport") && !sender.hasPermission("dropparty.list.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		DPTeleportsTable entry = DatabaseManager.getDatabase().select(DPTeleportsTable.class).where().equal("dpid", dpid).execute().findOne();
		if (entry == null) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dpnoteleportfound"), dpid);
			return;
		} else {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistteleport"), dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistteleport.world") + entry.world, dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistteleport.x") + entry.x, dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistteleport.y") + entry.y, dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistteleport.z") + entry.z, dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistteleport.pitch") + entry.pitch, dpid);
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dplistteleport.yaw") + entry.yaw, dpid);
		}
	}
}
