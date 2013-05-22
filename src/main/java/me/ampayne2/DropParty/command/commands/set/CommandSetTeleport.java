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

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.DPMessageController;
import me.ampayne2.DropParty.command.interfaces.DPCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DPPartiesTable;
import me.ampayne2.DropParty.database.tables.DPTeleportsTable;

public class CommandSetTeleport implements DPCommand {

	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!sender.hasPermission("dropparty.set.teleport") && !sender.hasPermission("dropparty.set.*") && !sender.hasPermission("dropparty.*")) {
			return;
		}
		Player player = (Player) sender;
		Location loc = player.getLocation();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		float pitch = loc.getPitch();
		float yaw = loc.getYaw();
		String dpid;
		if (args.length == 1) {
			dpid = args[0];
		} else {
			return;
		}
		if (DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne() == null
				|| !DatabaseManager.getDatabase().select(DPPartiesTable.class).where().equal("dpid", dpid).execute().findOne().dpid.equals(dpid)) {
			DPMessageController.sendMessage(player, DPMessageController.getMessage("dppartydoesntexist"), dpid);
			return;
		}
		DPTeleportsTable table = new DPTeleportsTable();
		table.dpid = dpid;
		table.world = player.getWorld().getName();
		table.x = x;
		table.y = y;
		table.z = z;
		table.pitch = pitch;
		table.yaw = yaw;
		try {
			if (DatabaseManager.getDatabase().select(DPTeleportsTable.class).where().equal("dpid", dpid).execute().findOne() == null) {
				DPTeleportsTable entry = DatabaseManager.getDatabase().select(DPTeleportsTable.class).where().equal("dpid", dpid).execute().findOne();
				table.id = entry.id;
			}
		} catch (NullPointerException ex) {
		}
		DatabaseManager.getDatabase().save(table);
		DPMessageController.sendMessage(player, DPMessageController.getMessage("dpsetteleport"), dpid);

	}
}