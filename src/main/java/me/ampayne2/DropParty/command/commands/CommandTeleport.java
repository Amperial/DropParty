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

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyPartiesTable;
import me.ampayne2.DropParty.database.tables.DropPartyTeleportsTable;

public class CommandTeleport implements DropPartyCommand{

	@Override
	public void execute(CommandSender sender, String[] args) {
		Player player = (Player) sender;
		if(!sender.hasPermission("dropparty.teleport")){
			return;
		}
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
		if(DatabaseManager.getDatabase().select(DropPartyTeleportsTable.class)
				.where().equal("dpid", dpid).execute().findOne() != null){
			 DropPartyTeleportsTable entry = DatabaseManager.getDatabase().select(DropPartyTeleportsTable.class).where().equal("dpid", dpid).execute().findOne();
			 World world = Bukkit.getServer().getWorld(entry.world);
			 Location loc = new Location(world, entry.x, entry.y, entry.z);
			 loc.setPitch(entry.pitch);
			 loc.setYaw(entry.yaw);
			 player.teleport(loc);
			 sender.sendMessage(ChatColor.AQUA + "Teleported To Drop Party " + dpid);
		}
	}
}
