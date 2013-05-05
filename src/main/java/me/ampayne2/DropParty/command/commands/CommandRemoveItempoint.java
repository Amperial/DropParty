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

import java.util.ArrayList;
import java.util.List;

import me.ampayne2.DropParty.command.interfaces.DropPartyCommand;
import me.ampayne2.DropParty.database.DatabaseManager;
import me.ampayne2.DropParty.database.tables.DropPartyItempointsTable;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandRemoveItempoint implements DropPartyCommand {

	private final static ArrayList<String> playersRemoving = new ArrayList<String>();

	public static void toggleRemoving(String playerName, CommandSender sender) {
		if (isRemoving(playerName)) {
			playersRemoving.remove(playerName);
			sender.sendMessage(ChatColor.AQUA
					+ "RemoveItemPoint mode deactivated.");
		} else {
			playersRemoving.add(playerName);
			sender.sendMessage(ChatColor.AQUA
					+ "RemoveItemPoint mode activated.");
		}
	}

	public static boolean isRemoving(String playerName) {
		return playersRemoving.contains(playerName);
	}

	public static void deleteItempoint(Player player, String playerName, int x,
			int y, int z) {
		DatabaseManager.getDatabase().remove(
				DatabaseManager.getDatabase()
						.select(DropPartyItempointsTable.class).where()
						.equal("world", player.getWorld().getName()).and()
						.equal("x", x).and().equal("y", y).and().equal("z", z)
						.execute().findOne());
		player.sendMessage(ChatColor.AQUA
				+ "Drop Party Item Point Removed Successfully.");

	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		String playerName = sender.getName();
		if (args.length == 1) {
			List<DropPartyItempointsTable> list = DatabaseManager.getDatabase()
					.select(DropPartyItempointsTable.class).execute().find();
			int itempointid;
			try {
				itempointid = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				sender.sendMessage(ChatColor.RED + "'" + args[0] + "'"
						+ " is not an integer.");
				return;
			}
			if (!(list.size() >= itempointid)) {
				sender.sendMessage(ChatColor.AQUA
						+ "There is no Item Point with the ID " + itempointid);
				return;
			}
			DropPartyItempointsTable entry = list.get(itempointid - 1);
			DatabaseManager.getDatabase().remove(
					DatabaseManager.getDatabase()
							.select(DropPartyItempointsTable.class).where()
							.equal("world", entry.world).and()
							.equal("x", entry.x).and().equal("y", entry.y)
							.and().equal("z", entry.z).execute().findOne());

			sender.sendMessage(ChatColor.AQUA
					+ "Drop Party Item Point Removed Successfully.");
			return;

		}
		if (args.length == 0 && CommandSetItempoint.isSetting(playerName)) {
			CommandSetItempoint.toggleSetting(playerName, sender);
		}
		toggleRemoving(playerName, sender);
	}
}
